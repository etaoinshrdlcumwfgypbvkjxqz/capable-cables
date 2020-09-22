package $group__.utilities;

import $group__.utilities.structures.Singleton;
import $group__.utilities.templates.CommonConfigurationTemplate;
import $group__.utilities.throwable.IThrowableHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.function.Supplier;

public final class UtilitiesConfiguration extends CommonConfigurationTemplate<UtilitiesConfiguration.ConfigurationData> {
	private static final Logger BOOTSTRAP_LOGGER = LoggerFactory.getLogger(UtilitiesConstants.MODULE_NAME);
	private static final UtilitiesConfiguration INSTANCE = Singleton.getSingletonInstance(UtilitiesConfiguration.class);

	private UtilitiesConfiguration() { super(getBootstrapLogger()); }

	public static UtilitiesConfiguration getInstance() { return INSTANCE; }

	public static Logger getBootstrapLogger() { return BOOTSTRAP_LOGGER; }

	public static final class ConfigurationData
			extends CommonConfigurationTemplate.ConfigurationData {
		public ConfigurationData(Logger logger,
		                         IThrowableHandler<Throwable> throwableHandler,
		                         Supplier<? extends Locale> localeSupplier) {
			super(logger, throwableHandler, localeSupplier);
		}
	}
}
