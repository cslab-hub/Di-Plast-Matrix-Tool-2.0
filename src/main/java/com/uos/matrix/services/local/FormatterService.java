package com.uos.matrix.services.local;

import com.uos.matrix.entities.RankableDoubleEntry;
import com.uos.matrix.entities.RankableIntegerEntry;
import com.uos.matrix.entities.RankableRangedEntry;
import com.uos.matrix.helper.enums.Triplet;
import com.uos.matrix.helper.enums.Units;
import com.uos.matrix.services.UnitConverterService;
import java.io.Serializable;
import java.util.Locale;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "formatterService")
@SessionScoped
public class FormatterService implements Serializable {

    @ManagedProperty(value = "#{unitConverterService}")
    private UnitConverterService unitConverterService;

    private static final long serialVersionUID = 1L;

    public Triplet getTriplet(String input) {
        if (input == null || input.toLowerCase().equals("null") || input.isBlank()) {
            return Triplet.UNK;
        } else if (Boolean.getBoolean(input)) {
            return Triplet.YES;
        } else {
            return Triplet.NO;
        }
    }

    public Integer getInteger(String string) {
        try {
            if (string == null || string.equals("NULL") || string.isBlank()) {
                return null;
            }
            return Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public Double getDouble(String string) {
        try {
            if (string == null || string.equals("NULL") || string.isBlank()) {
                return null;
            }
            return Double.parseDouble(string);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public Float getFloat(String string) {
        try {
            if (string == null || string.equals("NULL") || string.isBlank()) {
                return null;
            }
            return Float.parseFloat(string);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public String formatString(String string) {
        if (string.equals("NULL")) {
            return null;
        } else {
            return string;
        }
    }

    public String formatDouble(double value, int number) {
        return String.format(Locale.US, " %,." + number + "f", value);
    }

    public String formatVariationWithConversion(Object in, Units preferedUnit, int decimal) {
        Double value = null;
        Double variation = null;
        Units unit = null;

        if (in == null) {
            return "-";
        } else if (in instanceof RankableDoubleEntry) {
            RankableDoubleEntry d = (RankableDoubleEntry) in;
            value = d.getValue();
            variation = d.getVariation();
            unit = d.getUnit();
        } else if (in instanceof RankableIntegerEntry) {
            RankableIntegerEntry d = (RankableIntegerEntry) in;
            if (d.getValue() != null) {
                value = d.getValue().doubleValue();
            }

            if (d.getVariation() != null) {
                variation = d.getVariation().doubleValue();
            }

            unit = d.getUnit();
        } else if (in instanceof RankableRangedEntry) {
            RankableRangedEntry d = (RankableRangedEntry) in;
            value = d.getValueMin();
            variation = d.getVariation();
            unit = d.getUnit();
        }

        if (value == null || value == '-') {
            return "-";
        }
        if (unit == null || preferedUnit == null) {
            if (variation == null || variation == '-') {
                return formatOutput(value, decimal);
            }
            return formatOutput(value, decimal) + "±" + formatOutput(variation, decimal);
        }
        if (variation != null) {
            return formatOutput(unitConverterService.convertUnit(value, preferedUnit, unit), decimal) + "±" + formatOutput(unitConverterService.convertUnit(variation, preferedUnit, unit), decimal);
        } else {
            return formatOutput(unitConverterService.convertUnit(value, preferedUnit, unit), decimal);
        }
    }

    public String formatOutput(Object in, int decimal) {
        if (in == null) {
            return "-";
        } else if (in instanceof Double) {
            Double input = (Double) in;
            return String.format(Locale.US, "%,." + decimal + "f", input);
        } else if (in instanceof Integer) {
            Integer input = (Integer) in;
            return String.format(Locale.US, "%,." + decimal + "f", Double.valueOf(input));
        } else if (in instanceof Float) {
            Float input = (Float) in;
            return String.format(Locale.US, "%,." + decimal + "f", input);
        } else if (in instanceof String) {
            String input = (String) in;
            return input;
        } else {
            return in.toString();
        }
    }

    public String formatRangeFilterOutput(double min, double max, double currentMin, double currentMax, int decimal) {
        if (min >= currentMin && max <= currentMax) {
            return "All";
        }
        return formatDouble(currentMin, decimal) + " - " + formatDouble(currentMax, decimal);
    }

    public String formatUnit(RankableDoubleEntry u) {
        return formatUnit(u, false);
    }

    public String formatUnit(RankableIntegerEntry u) {
        return formatUnit(u, false);
    }

    public String formatUnit(RankableRangedEntry u) {
        return formatUnit(u, false);
    }

    public String formatUnit(RankableDoubleEntry u, Units preferredUnit) {
        return formatUnit(u, false, preferredUnit);
    }

    public String formatUnit(RankableIntegerEntry u, Units preferredUnit) {
        return formatUnit(u, false, preferredUnit);
    }

    public String formatUnit(RankableRangedEntry u, Units preferredUnit) {
        return formatUnit(u, false, preferredUnit);
    }

    public String formatUnit(RankableDoubleEntry u, boolean brackets) {
        return formatUnit(u, brackets, u.getUnit());
    }

    public String formatUnit(RankableIntegerEntry u, boolean brackets) {
        return formatUnit(u, brackets, u.getUnit());
    }

    public String formatUnit(RankableRangedEntry u, boolean brackets) {
        return formatUnit(u, brackets, u.getUnit());
    }

    public String formatUnit(RankableDoubleEntry u, boolean brackets, Units preferredUnit) {
        if (u == null || u.getUnit() == null) {
            return "";
        } else {
            if (brackets) {
                return "(" + preferredUnit.getName() + ")";
            }
            if (u.getValue() == null) {
                return "";
            }
            return preferredUnit.getName();
        }
    }

    public String formatUnit(RankableIntegerEntry u, boolean brackets, Units preferredUnit) {
        if (u == null || u.getUnit() == null) {
            return "";
        } else {
            if (brackets) {
                return "(" + preferredUnit.getName() + ")";
            }
            if (u.getValue() == null) {
                return "";
            }
            return preferredUnit.getName();
        }
    }

    public String formatUnit(RankableRangedEntry u, boolean brackets, Units preferredUnit) {
        if (u == null || u.getUnit() == null) {
            return "";
        } else {
            if (brackets) {
                return "(" + preferredUnit.getName() + ")";
            }
            if (u.getValueMin() == null) {
                return "";
            }
            return preferredUnit.getName();
        }
    }

    public void setUnitConverterService(UnitConverterService unitConverterService) {
        this.unitConverterService = unitConverterService;
    }

}
