package com.uos.matrix.services;

import com.uos.matrix.entities.RankableDoubleEntry;
import com.uos.matrix.entities.RankableRangedEntry;
import com.uos.matrix.helper.enums.Units;
import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "unitConverterService")
@SessionScoped
public class UnitConverterService implements Serializable {

    private static final long serialVersionUID = 1L;

    public Double convertUnit(RankableDoubleEntry u, Units preferredUnit) {
        return convertUnit(u.getValue(), preferredUnit, u.getUnit());
    }

    public RankableRangedEntry convertUnit(RankableRangedEntry u, Units preferredUnit) {
        u.setValueMin(convertUnit(u.getValueMin(), preferredUnit, u.getUnit()));
        u.setValueMax(convertUnit(u.getValueMax(), preferredUnit, u.getUnit()));
        return u;
    }
    
    
    public Double convertUnit(Double u, Units preferredUnit, Units usedUnit) {
        if (preferredUnit == null || u == null || usedUnit == null) {
            return null;
        }
        switch (preferredUnit) {
            case MPA:
            case NPMM2:
                switch (usedUnit) {
                    case PSI:
                        return u / 145;
                    default:
                        return u;
                }
            case PSI:
                switch (usedUnit) {
                    case MPA:
                    case NPMM2:
                        return u * 145;
                    default:
                        return u;
                }
            case GRPCM3:
            case KGPLTR:
                switch (usedUnit) {
                    case KGPM3:
                    case GRPLTR:
                        return u / 1000;
                    default:
                        return u;
                }
            case KGPM3:
            case GRPLTR:
                switch (usedUnit) {
                    case KGPLTR:
                    case GRPCM3:
                        return u * 1000;
                    default:
                        return u;
                }
            case CELSIUS:
                switch (usedUnit) {
                    case FAHRENHEIT:
                        return ((u - 32) * 5/9);
                    default:
                        return u;
                }
            case FAHRENHEIT:
                switch (usedUnit) {
                    case CELSIUS:
                        return ((u * 9/5) + 32);
                    default:
                        return u;
                }
            case KJPM:
            case JPMM:
                switch (usedUnit) {
                    case JPM:
                        return u / 1000;
                    case JPCM:
                        return u / 10;
                    case FTLB:
                        return u / 18.734;
                    default:
                        return u;
                }
            case JPM:
                switch (usedUnit) {
                    case KJPM:
                    case JPMM:
                        return u * 1000;
                    case JPCM:
                        return u * 100;
                    case FTLB:
                        return u * 53.3787;
                    default:
                        return u;
                }
            case JPCM:
                switch (usedUnit) {
                    case JPM:
                        return u / 100;
                    case JPMM:
                    case KJPM:
                        return u * 10;
                    case FTLB:
                        return u / 1.8734;
                    default:
                        return u;
                }
            case FTLB:
                switch (usedUnit) {
                    case JPMM:
                    case KJPM:
                        return u * 18.734;
                    case JPM:
                        return u / 53.3787;
                    case JPCM:
                        return u * 1.8734;

                }
            case KJPM2:
                switch (usedUnit) {
                    case JPM2:
                        return u / 1000;
                    case JPCM2:
                        return u * 10;
                    case JPMM2:
                        return u * 1000;
                    case FTLB2:
                        return u * 2.10152;
                    default:
                        return u;
                }
            case JPM2:
                switch (usedUnit) {
                    case KJPM2:
                        return u * 1000;
                    case JPCM2:
                        return u * 10000;
                    case JPMM2:
                        return u * 1000000;
                    case FTLB2:
                        return u * 2101.52;
                    default:
                        return u;
                }
            case JPCM2:
                switch (usedUnit) {
                    case KJPM2:
                        return u / 10;
                    case JPM2:
                        return u / 10000;
                    case JPMM2:
                        return u * 100;
                    case FTLB2:
                        return u / 4.75846;
                }
            case JPMM2:
                switch (usedUnit) {
                    case KJPM2:
                        return u / 1000;
                    case JPM2:
                        return u / 1000000;
                    case JPCM2:
                        return u / 100;
                    case FTLB2:
                        return u / 475.846;
                }
            case FTLB2:
               switch (usedUnit) {
                   case KJPM2:
                       return u / 2.10152;
                   case JPM2:
                       return u / 2101.52;
                   case JPCM2:
                       return u * 4.75846;
                   case JPMM2:
                       return u * 475.846;
                   default:
                       return u;
               }
            default:
                return u;
        }
    }

    public int getFirstSignificantDigitPos(double number) {
        if (number < 1) {
            return (int) Math.abs(Math.ceil(-Math.log10(number)));
        } else {
            return 0;
        }
    }

}
