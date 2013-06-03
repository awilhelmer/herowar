package importer;

import org.apache.commons.beanutils.converters.AbstractConverter;

import play.Logger;

/**
 * @author Sebastian Sachtleben
 */
public class EnumConverter extends AbstractConverter {

  private final static Logger.ALogger log = Logger.of(EnumConverter.class);

  @Override
  protected String convertToString(final Object pValue) throws Throwable {
    return ((Enum<?>) pValue).name();
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  protected Object convertToType(final Class pType, final Object pValue) throws Throwable {
    final Class<? extends Enum> type = pType;
    try {
      return Enum.valueOf(type, pValue.toString());
    } catch (final IllegalArgumentException e) {
      log.warn("No enum value \"" + pValue + "\" for " + type.getName());
    }
    return null;
  }

  @SuppressWarnings("rawtypes")
  @Override
  protected Class getDefaultType() {
    return null;
  }
}
