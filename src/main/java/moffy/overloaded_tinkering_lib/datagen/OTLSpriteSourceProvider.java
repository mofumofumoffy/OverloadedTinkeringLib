package moffy.overloaded_tinkering_lib.datagen;

import moffy.overloaded_tinkering_lib.OverloadedTinkeringLib;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;

public class OTLSpriteSourceProvider extends SpriteSourceProvider {
    private static final String[] TRIMS = {
            "coast", "sentry", "dune", "wild", "ward", "eye", "vex", "tide", "snout",
            "rib", "spire", "wayfinder", "shaper", "silence", "raiser", "host"
    };

    public OTLSpriteSourceProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper, OverloadedTinkeringLib.MODID);
    }

    @Override
    protected void addSources() {
        String paletteFolder = "trims/color_palettes/";



        atlas(BLOCKS_ATLAS)
                .addSource(new DirectoryLister("entity", "entity/"))
                .addSource(new DirectoryLister("tinker_armor", "tinker_armor/"))
                .addSource(new DirectoryLister("obj_tool", "obj_tool/"));

        atlas(ResourceLocation.parse("armor_trims"))
                .addSource(new DirectoryLister("misc", "misc/"));
    }
}
