package tehnut.stable;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public enum EnumStability {

    STABLE("desc.stability.stable"),
    SEMISTABLE("desc.stability.semistable"),
    UNSTABLE("desc.stability.unstable"),
    DANGEROUS("desc.stability.dangerous");

    private String descKey;

    EnumStability(String descKey) {
        this.descKey = descKey;
    }

    public String getDescription() {
        return descKey;
    }

    public String getFancyDescription() {
        return StatCollector.translateToLocal(getDescription()).replace("&", "\u00A7");
    }

    public String getLocalizedDescription() {
        return EnumChatFormatting.getTextWithoutFormattingCodes(getFancyDescription());
    }
}
