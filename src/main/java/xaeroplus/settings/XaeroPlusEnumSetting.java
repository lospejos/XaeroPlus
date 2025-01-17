package xaeroplus.settings;

import net.minecraft.client.option.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;
import xaeroplus.XaeroPlus;
import xaeroplus.settings.XaeroPlusSettingsReflectionHax.SettingLocation;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

public class XaeroPlusEnumSetting<T extends Enum<T>> extends XaeroPlusSetting {
    private final T[] enumValues;
    private T value;
    private Consumer<T> settingChangeConsumer;

    private XaeroPlusEnumSetting(final String settingName,
                                 final String settingNameTranslationKey,
                                 final String tooltipTranslationKey,
                                 final KeyBinding keyBinding,
                                 final Supplier<Boolean> visibilitySupplier,
                                 final Consumer<T> settingChangeConsumer,
                                 final T[] enumValues,
                                 final T defaultValue) {
        super(settingName, settingNameTranslationKey, tooltipTranslationKey, keyBinding, visibilitySupplier);
        this.enumValues = enumValues;
        this.value = defaultValue;
        this.settingChangeConsumer = settingChangeConsumer;
    }

    public static <E extends Enum<E>> XaeroPlusEnumSetting<E> create(String settingName,
                                                                     String settingNameTranslationKey,
                                                                     String tooltipTranslationKey,
                                                                     E[] values,
                                                                     E defaultValue,
                                                                     final SettingLocation settingLocation) {
        final XaeroPlusEnumSetting<E> setting = new XaeroPlusEnumSetting<>(SETTING_PREFIX + settingName,
                                                                           settingNameTranslationKey,
                                                                           tooltipTranslationKey,
                                                                           null,
                                                                           null,
                                                                           null,
                                                                           values,
                                                                           defaultValue);
        settingLocation.getSettingsList().add(setting);
        return setting;
    }

    public static <E extends Enum<E>> XaeroPlusEnumSetting<E> create(String settingName,
                                                                     String settingNameTranslationKey,
                                                                     String tooltipTranslationKey,
                                                                     Consumer<E> settingChangeConsumer,
                                                                     E[] values,
                                                                     E defaultValue,
                                                                     final SettingLocation settingLocation) {
        final XaeroPlusEnumSetting<E> setting = new XaeroPlusEnumSetting<>(SETTING_PREFIX + settingName,
                                                                           settingNameTranslationKey,
                                                                           tooltipTranslationKey,
                                                                           null,
                                                                           null,
                                                                           settingChangeConsumer,
                                                                           values,
                                                                           defaultValue);
        settingLocation.getSettingsList().add(setting);
        return setting;
    }

    public static <E extends Enum<E>> XaeroPlusEnumSetting<E> create(String settingName,
                                                                     String settingNameTranslationKey,
                                                                     String tooltipTranslationKey,
                                                                     Supplier<Boolean> visibilitySupplier,
                                                                     Consumer<E> settingChangeConsumer,
                                                                     E[] values,
                                                                     E defaultValue,
                                                                     final SettingLocation settingLocation) {
        final XaeroPlusEnumSetting<E> setting = new XaeroPlusEnumSetting<>(SETTING_PREFIX + settingName,
                                                                           settingNameTranslationKey,
                                                                           tooltipTranslationKey,
                                                                           null,
                                                                           visibilitySupplier,
                                                                           settingChangeConsumer,
                                                                           values,
                                                                           defaultValue);
        settingLocation.getSettingsList().add(setting);
        return setting;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T newVal) {
        this.value = newVal;
        if (nonNull(getSettingChangeConsumer())) {
            try {
                getSettingChangeConsumer().accept(newVal);
            } catch (final Exception e) {
                XaeroPlus.LOGGER.error("Error applying setting change consumer for setting: {}, value: {}", getSettingName(), newVal, e);
            }

        }
    }

    public int getValueIndex() {
        return ArrayUtils.indexOf(enumValues, getValue());
    }

    public void setValueIndex(final int index) {
        try {
            setValue(enumValues[index]);
        } catch (final Exception e) {
            XaeroPlus.LOGGER.error("Unable to set enum value setting for {}, index {}", getSettingName(), index, e);
        }
    }

    public int getIndexMax() {
        return enumValues.length-1;
    }

    public void setSettingChangeConsumer(final Consumer<T> settingChangeConsumer) {
        this.settingChangeConsumer = settingChangeConsumer;
    }

    public Consumer<T> getSettingChangeConsumer() {
        return settingChangeConsumer;
    }

    public T[] getEnumValues() {
        return enumValues;
    }
    @Override
    public void init() {
        if (nonNull(settingChangeConsumer)) {
            settingChangeConsumer.accept(value);
        }
    }
}
