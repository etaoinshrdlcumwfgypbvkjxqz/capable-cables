package $group__.$modId__;

import $group__.$modId__.client.configurations.ModGuiFactoryThis;
import $group__.$modId__.common.registrable.items.ItemWrench;
import $group__.$modId__.proxies.Proxy;
import $group__.$modId__.proxies.ProxyClient;
import $group__.$modId__.proxies.ProxyNull;
import $group__.$modId__.proxies.ProxyServer;
import $group__.$modId__.utilities.constructs.classes.Singleton;
import $group__.$modId__.utilities.constructs.classes.concrete.ModContainerNull;
import $group__.$modId__.utilities.constructs.interfaces.annotations.Marker;
import $group__.$modId__.utilities.constructs.interfaces.annotations.Property;
import $group__.$modId__.utilities.helpers.Reflections.Classes.AccessibleObjectAdapter.FieldAdapter;
import $group__.$modId__.utilities.variables.Globals;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Reflections.Classes.AccessibleObjectAdapter.FieldAdapter.setModifiers;
import static $group__.$modId__.utilities.helpers.Reflections.Classes.AccessibleObjectAdapter.setAccessibleWithLogging;
import static $group__.$modId__.utilities.helpers.Reflections.getPackage;
import static $group__.$modId__.utilities.helpers.Reflections.isMemberStatic;
import static $group__.$modId__.utilities.helpers.Throwables.requireRunOnceOnly;
import static $group__.$modId__.utilities.variables.Constants.*;
import static $group__.$modId__.utilities.variables.Globals.REFLECTIONS_CACHE;
import static $group__.$modId__.utilities.variables.Globals.rethrowCaughtThrowableStatic;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static net.minecraftforge.fml.common.Mod.EventHandler;
import static net.minecraftforge.fml.common.Mod.InstanceFactory;

@Mod(
		modid = MOD_ID,
		name = NAME,
		version = VERSION,
		dependencies = DEPENDENCIES,
		useMetadata = true,
		acceptedMinecraftVersions = ACCEPTED_MINECRAFT_VERSIONS,
		certificateFingerprint = CERTIFICATE_FINGERPRINT,
		modLanguageAdapter = PACKAGE + '.' + ModThis.CLASS_SIMPLE_NAME + '$' + ModThis.CustomLanguageAdapter.CLASS_SIMPLE_NAME,
		guiFactory = PACKAGE + '.' + ModGuiFactoryThis.SUBPACKAGE + '.' + ModGuiFactoryThis.CLASS_SIMPLE_NAME,
		updateJSON = UPDATE_JSON
)
public enum ModThis {
	/* SECTION enums */
	INSTANCE;


	/* SECTION static variables */

	public static final String
			CLASS_SIMPLE_NAME = "ModThis",
			EVENT_PROCESSOR_VALUE = "event processor",
			CONTAINER_VALUE = "container";
	public static final Logger LOGGER = LogManager.getLogger(NAME + '|' + MOD_ID);
	@Marker(@Property(key = "Field", value = CONTAINER_VALUE))
	public static final ModContainer CONTAINER = Singleton.getInstance(ModContainerNull.class);
	@SidedProxy(modId = MOD_ID,
			clientSide = PACKAGE + '.' + Proxy.SUBPACKAGE + '.' + ProxyClient.CLASS_SIMPLE_NAME,
			serverSide = PACKAGE + '.' + Proxy.SUBPACKAGE + '.' + ProxyServer.CLASS_SIMPLE_NAME)
	public static final Proxy PROXY = Singleton.getInstance(ProxyNull.class);
	private static final ImmutableMap<Class<? extends FMLEvent>, BiConsumer<?, ? extends FMLEvent>> EVENT_MAP = new ImmutableMap.Builder<Class<? extends FMLEvent>, BiConsumer<?, ? extends FMLEvent>>()
			.put(FMLConstructionEvent.class, (ModThis m, FMLConstructionEvent e) ->
					processEvent("Construction", m, e, PROXY::construct))
			.put(FMLPreInitializationEvent.class, (ModThis m, FMLPreInitializationEvent e) ->
					processEvent("Pre-initialization", m, e, PROXY::preInitialize))
			.put(FMLInitializationEvent.class, (ModThis m, FMLInitializationEvent e) ->
					processEvent("Initialization", m, e, PROXY::initialize))
			.put(FMLPostInitializationEvent.class, (ModThis m, FMLPostInitializationEvent e) ->
					processEvent("Post-initialization", m, e, PROXY::postInitialize))
			.put(FMLLoadCompleteEvent.class, (ModThis m, FMLLoadCompleteEvent e) ->
					processEvent("Load completion", m, e, PROXY::completeLoading))
			.put(FMLServerAboutToStartEvent.class, (ModThis m, FMLServerAboutToStartEvent e) ->
					processEvent("Server pre-starting", m, e, PROXY::preStartServer))
			.put(FMLServerStartingEvent.class, (ModThis m, FMLServerStartingEvent e) ->
					processEvent("Server starting", m, e, PROXY::startServer))
			.put(FMLServerStartedEvent.class, (ModThis m, FMLServerStartedEvent e) ->
					processEvent("Server post-starting", m, e, PROXY::postStartServer))
			.put(FMLServerStoppingEvent.class, (ModThis m, FMLServerStoppingEvent e) ->
					processEvent("Server stopping", m, e, PROXY::stopServer))
			.put(FMLServerStoppedEvent.class, (ModThis m, FMLServerStoppedEvent e) ->
					processEvent("Server post-stopping", m, e, PROXY::postStopServer))
			.put(FMLFingerprintViolationEvent.class, (ModThis m, FMLFingerprintViolationEvent e) ->
					processEvent("Fingerprint violation", m, e, PROXY::violateFingerprint))
			.put(FMLInterModComms.IMCEvent.class, (ModThis m, FMLInterModComms.IMCEvent e) ->
					processEvent("Inter-mod communication messages processing", m, e, PROXY::processIMCMessages))
			.put(FMLModIdMappingEvent.class, (ModThis m, FMLModIdMappingEvent e) ->
					processEvent("ID mapping processing", m, e, PROXY::processIDMapping))
			.put(FMLModDisabledEvent.class, (ModThis m, FMLModDisabledEvent e) ->
					processEvent("Disabling", m, e, PROXY::disable)).build();


	/* SECTION static methods */

	@SuppressWarnings("SameReturnValue")
	@InstanceFactory
	public static ModThis getInstance() { return INSTANCE; }

	private static <M, E> void processEvent(String name, M mod, E event, BiConsumer<? super M, ? super E> process) {
		LOGGER.info("{} started", name);
		Stopwatch stopwatch = Stopwatch.createStarted();
		process.accept(mod, event);
		LOGGER.info("{} ended ({} <- {} ns)", name, stopwatch.stop(), stopwatch.elapsed(NANOSECONDS));
	}


	/* SECTION methods */

	@Marker(@Property(key = "Method", value = EVENT_PROCESSOR_VALUE))
	@EventHandler
	public void processEvent(FMLEvent event) { EVENT_MAP.getOrDefault(event.getClass(), (m, e) -> LOGGER.info("FMLEvent '{}' received, but processing is NOT implemented", e)).accept(castUncheckedUnboxedNonnull(this), castUncheckedUnboxedNonnull(event)); }


	/* SECTION static classes */

	@Config(modid = MOD_ID, name = NAME, category = "General")
	@Config.LangKey(Configuration.LANG_KEY_BASE + ".name")
	public enum Configuration {
		;
		@Config.Ignore
		public static final String LANG_KEY_BASE = "configurations." + MOD_ID;
		@Config.LangKey(Behavior.LANG_KEY_BASE + ".name")
		public static final Behavior behavior = new Behavior();
		@SuppressWarnings("unused")
		@Config.Comment({"1st line", "2nd line"})
		@Config.Name("Template") // COMMENT overridden by @Config.LangKey
		@Config.LangKey("template.template.template_") // COMMENT overrides @Config.Name
		@Config.RequiresMcRestart
		@Config.RequiresWorldRestart
		@Config.Ignore
		private static final String TEMPLATE_ = "default";
		@SuppressWarnings("unused")
		@Config.RangeInt(min = -1337, max = 1337)
		@Config.Ignore
		private static final int INT_TEMPLATE_ = 69;
		@SuppressWarnings("unused")
		@Config.RangeDouble(min = -13.37D, max = 13.37D)
		@Config.Ignore
		private static final double DOUBLE_TEMPLATE_ = 6.9D;

		public static final class Behavior {
			public static final String LANG_KEY_BASE = Configuration.LANG_KEY_BASE + ".behavior";
			@Config.LangKey(Items.LANG_KEY_BASE + ".name")
			public final Items items = new Items();

			private Behavior() { requireRunOnceOnly(); }

			public static final class Items {
				public static final String LANG_KEY_BASE = Behavior.LANG_KEY_BASE + ".items";
				@Config.LangKey(ItemWrench.Configuration.LANG_KEY_BASE + ".name")
				public final ItemWrench.Configuration wrench = new ItemWrench.Configuration();

				private Items() { requireRunOnceOnly(); }
			}
		}
	}

	public static class CustomLanguageAdapter extends ILanguageAdapter.JavaAdapter {
		/* SECTION static variables */

		public static final String CLASS_SIMPLE_NAME = "CustomLanguageAdapter";


		/* SECTION methods */

		@Override
		public Object getNewInstance(FMLModContainer container, Class<?> objectClass, ClassLoader classLoader, Method factoryMarkedMethod) throws Exception {
			Object r = super.getNewInstance(container, objectClass, classLoader, factoryMarkedMethod);

			// COMMENT get container
			for (Field f : objectClass.getDeclaredFields()) {
				if (isMemberStatic(f)) {
					Marker m = f.getDeclaredAnnotation(Marker.class);
					if (m != null) {
						Property[] mps = m.value();
						if (mps.length == 1) {
							Property mp = mps[0];
							Class<? extends Field> fc = f.getClass();
							if (mp.key().equals(fc.getSimpleName()) && mp.value().equals(CONTAINER_VALUE)) {
								FieldAdapter fa = FieldAdapter.of(f);
								setAccessibleWithLogging(fa, "container field", null, fc, true);
								setModifiers(fa, f.getModifiers() & ~Modifier.FINAL);
								if (!fa.set(null, container)) throw rethrowCaughtThrowableStatic();
								break;
							}
						}
					}
				}
			}

			// COMMENT modify event methods
			for (Method m : objectClass.getDeclaredMethods()) {
				if (!isMemberStatic(m)) {
					Marker mm = m.getDeclaredAnnotation(Marker.class);
					if (mm != null && m.getDeclaredAnnotation(EventHandler.class) != null) {
						Property[] mmPs = mm.value();
						if (mmPs.length == 1) {
							Property mp = mmPs[0];
							if (mp.key().equals(m.getClass().getSimpleName()) && mp.value().equals(EVENT_PROCESSOR_VALUE)) {
								Class<? extends FMLModContainer> containerC = container.getClass();
								for (Field f : containerC.getDeclaredFields()) {
									if (ListMultimap.class.isAssignableFrom(f.getType()) && !isMemberStatic(f)) {
										FieldAdapter fa = FieldAdapter.of(f);
										ListMultimap<Class<? extends FMLEvent>, Method> fv;

										if (!fa.setAccessible(true)) throw rethrowCaughtThrowableStatic();
										fv = castUncheckedUnboxedNonnull(fa.get(container).orElseThrow(Globals::rethrowCaughtThrowableStatic));

										REFLECTIONS_CACHE.get(getPackage(FMLEvent.class)).getSubTypesOf(FMLEvent.class).forEach(t -> fv.put(t, m));

										break;
									}
								}
								break;
							}
						}
					}
				}
			}

			return r;
		}

		@Override
		public void setProxy(Field target, Class<?> proxyTarget, Object proxy) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
			setModifiers(FieldAdapter.of(target), target.getModifiers() & ~Modifier.FINAL);
			super.setProxy(target, proxyTarget, proxy);
		}
	}
}
