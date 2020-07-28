package $group__.client.gui.utilities;

import $group__.utilities.specific.Loggers;
import $group__.utilities.specific.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

import static $group__.utilities.Capacities.INITIAL_CAPACITY_2;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public enum GLUtilities {
	;

	public static final int
			GL_MASK_ALL_BITS = 0xFFFFFFFF;

	public static Point2D getCursorPos() {
		double[] xPos = new double[1], yPos = new double[1];
		GLFW.glfwGetCursorPos(getWindowHandle(), xPos, yPos);
		return new Point2D.Double(xPos[0], yPos[0]);
	}

	public static long getWindowHandle() { return Minecraft.getInstance().getMainWindow().getHandle(); }

	@OnlyIn(CLIENT)
	public enum GLStacks {
		;

		public static final Runnable GL_SCISSOR_FALLBACK = () -> {
			MainWindow window = Minecraft.getInstance().getMainWindow();
			GLState.setIntegerValue(GL11.GL_SCISSOR_BOX, new int[]{0, 0, window.getFramebufferWidth(), window.getFramebufferHeight()}, (i, v) -> GL11.glScissor(v[0], v[1], v[2], v[3]));
		},
				STENCIL_MASK_FALLBACK = () -> RenderSystem.stencilMask(GLUtilities.GL_MASK_ALL_BITS),
				STENCIL_FUNC_FALLBACK = () -> RenderSystem.stencilFunc(GL11.GL_ALWAYS, 0, GLUtilities.GL_MASK_ALL_BITS),
				STENCIL_OP_FALLBACK = () -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP),
				COLOR_MASK_FALLBACK = () -> RenderSystem.colorMask(true, true, true, true);
		private static final Logger LOGGER = LogManager.getLogger();


		private static final ConcurrentMap<String, Deque<GLCall>> STACKS = Maps.MAP_MAKER_SINGLE_THREAD.makeMap();

		public static void push(String name, Runnable action, Runnable fallback) {
			getStack(name).push(new GLCall(action, fallback));
			action.run();
		}

		public static void pop(String name) {
			Deque<GLCall> stack = getStack(name);
			Runnable fallback = stack.pop().fallback;
			(stack.isEmpty() ? fallback : stack.peek()).run();
		}

		public static void clearAll() {
			STACKS.keySet().forEach(GLStacks::clear);
			STACKS.clear();
		}

		public static void clear(String name) {
			Deque<GLCall> stack = getStack(name);
			if (!stack.isEmpty()) {
				LOGGER.warn(Loggers.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE.makeMessage("{} leak: {}: {} not popped", GLStacks.class.getSimpleName(), name, stack.size()));
				while (!stack.isEmpty())
					pop(name);
			}
		}

		private static Deque<GLCall> getStack(String name) { return STACKS.computeIfAbsent(name, s -> new ArrayDeque<>(INITIAL_CAPACITY_2)); }

		@OnlyIn(CLIENT)
		private static class GLCall implements Runnable {
			private final Runnable action, fallback;

			private GLCall(Runnable action, Runnable fallback) {
				this.action = action;
				this.fallback = fallback;
			}

			@Override
			public void run() { action.run(); }
		}
	}

	@OnlyIn(CLIENT)
	public enum GLState {
		;

		private static final ConcurrentMap<Integer, Object> STATE = Maps.MAP_MAKER_SINGLE_THREAD.makeMap();

		public static int getInteger(int name) { return (int) STATE.computeIfAbsent(name, GL11::glGetInteger); }

		public static void setInteger(int name, int param, BiConsumer<Integer, Integer> setter) {
			setter.accept(name, param);
			STATE.put(name, param);
		}

		public static void getIntegerValue(int name, int[] params) {
			int[] ret = (int[]) STATE.computeIfAbsent(name, n -> {
				int[] p = new int[params.length];
				GL11.glGetIntegerv(n, p);
				return p;
			});
			System.arraycopy(ret, 0, params, 0, params.length);
		}

		public static void setIntegerValue(int name, int[] params, BiConsumer<Integer, int[]> setter) {
			setter.accept(name, params);
			STATE.put(name, params.clone());
		}
	}
}
