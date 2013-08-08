package jobs;

import org.apache.commons.beanutils.ConvertUtilsBean2;
import org.apache.commons.beanutils.Converter;

/**
 * @author Sebastian Sachtleben
 */
public class EnumAwareConvertUtilsBean extends ConvertUtilsBean2 {

	private static final EnumConverter ENUM_CONVERTER = new EnumConverter();

	@SuppressWarnings("rawtypes")
	@Override
	public Converter lookup(Class pClazz) {
		final Converter converter = super.lookup(pClazz);
		if (converter == null && pClazz.isEnum()) {
			return ENUM_CONVERTER;
		} else {
			return converter;
		}
	}

}
