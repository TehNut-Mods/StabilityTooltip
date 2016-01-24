package tehnut.stabilitytooltip;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Mod(modid = StabilityTooltip.MODID, name = StabilityTooltip.NAME, version = StabilityTooltip.VERSION, dependencies = "after:Waila", canBeDeactivated = true, acceptedMinecraftVersions = "[1.8,)")
public class StabilityTooltip {

    public static final String MODID = "StabilityTooltip";
    public static final String NAME = "StabilityTooltip";
    public static final String VERSION = "@VERSION@";

    public Map<String, EnumStability> stabilityMap = new HashMap<String, EnumStability>();

    @Mod.Instance
    public static StabilityTooltip instance;

    public File jsonConfig;
    public Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(instance);

        try {
            jsonConfig = new File(event.getModConfigurationDirectory(), NAME + ".json");

            if (!jsonConfig.exists() && jsonConfig.createNewFile()) {
                Map<String, EnumStability> defaultMap = new HashMap<String, EnumStability>();
                for (ModContainer modContainer : Loader.instance().getActiveModList())
                    defaultMap.put(modContainer.getModId(), EnumStability.STABLE);
                defaultMap.put("minecraft", EnumStability.STABLE);
                String json = gson.toJson(defaultMap, new TypeToken<Map<String, EnumStability>>(){ }.getType());
                FileWriter writer = new FileWriter(jsonConfig);
                writer.write(json);
                writer.close();
            }

            stabilityMap = gson.fromJson(new FileReader(jsonConfig), new TypeToken<Map<String, EnumStability>>(){ }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.itemStack;

        EnumStability stability = stabilityMap.get(getModidFromStack(stack));

        if (stability != null)
            event.toolTip.add(String.format(StatCollector.translateToLocal("desc.stability.format").replace("&", "\u00A7"), stability.getFancyDescription()));
    }

    private static String getModidFromStack(ItemStack stack) {
        Item check = stack.getItem();
        ResourceLocation info = Item.itemRegistry.getNameForObject(check);

        return info.toString().split(":")[0];
    }

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
}
