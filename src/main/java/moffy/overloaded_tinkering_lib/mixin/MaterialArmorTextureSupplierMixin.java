package moffy.overloaded_tinkering_lib.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moffy.overloaded_tinkering_lib.Config;
import moffy.overloaded_tinkering_lib.client.CustomTinkerRenders;
import moffy.overloaded_tinkering_lib.client.provider.ShaderProvider;
import moffy.overloaded_tinkering_lib.client.shader.TintedShaderArmorTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.client.armor.texture.ArmorTextureSupplier;
import slimeknights.tconstruct.library.client.armor.texture.MaterialArmorTextureSupplier;
import slimeknights.tconstruct.library.client.armor.texture.TintedArmorTexture;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;

import java.util.Optional;

@Mixin(value = MaterialArmorTextureSupplier.class, remap = false)
@Debug(export = true)
public abstract class MaterialArmorTextureSupplierMixin {


    /*@ModifyExpressionValue(method = "materialGetter", at = @At(value = "INVOKE", target = "Lslimeknights/mantle/data/listener/ResourceValidator;test(Lnet/minecraft/resources/ResourceLocation;)Z"))
    private static boolean ignoreValidator(boolean original) {
        return true;
    }*/

    @ModifyReturnValue(method = "lambda$materialGetter$2", at = {
            @At(value = "RETURN", ordinal = 0),
            @At(value = "RETURN", ordinal = 1)
    })
    private static ArmorTextureSupplier.ArmorTexture materialGetterExtension(ArmorTextureSupplier.ArmorTexture original,
                                                                             @Local(argsOnly = true) ResourceLocation name,
                                                                             @Local MaterialVariantId materialVariantId,
                                                                             @Local(ordinal = 0) int color) {
        ShaderProvider.Armor shaderProvider = CustomTinkerRenders.ARMOR_SHADERS.getShaderProvider(materialVariantId);
        if(shaderProvider == null) return original;

        Material textureMaterial = new Material(InventoryMenu.BLOCK_ATLAS, ticex$getAtlasLocation(name));
        return ticex$getArmorTexture(original, textureMaterial, materialVariantId, color, shaderProvider);
    }

    @Inject(method = "lambda$materialGetter$2", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z"), cancellable = true)
    private static void materialGetterExtension$3(ResourceLocation name, String materialStr, CallbackInfoReturnable<ArmorTextureSupplier.ArmorTexture> cir,
                                                  @Local MaterialVariantId materialId) {
        Optional<MaterialRenderInfo> infoOptional = MaterialRenderInfoLoader.INSTANCE.getRenderInfo(materialId);
        if(infoOptional.isEmpty()) {
            ShaderProvider.Armor shaderProvider = CustomTinkerRenders.ARMOR_SHADERS.getShaderProvider(materialId);
            if(shaderProvider == null) return;

            Material textureMaterial = new Material(InventoryMenu.BLOCK_ATLAS, ticex$getAtlasLocation(name));

            cir.setReturnValue(
                    ticex$getArmorTexture(
                            new TintedArmorTexture(ArmorTextureSupplier.getTexturePath(name), -1),
                            textureMaterial,
                            materialId,
                            -1,
                            shaderProvider
                    )
            );
        }
    }

    @Unique
    private static ArmorTextureSupplier.ArmorTexture ticex$getArmorTexture(ArmorTextureSupplier.ArmorTexture texture, Material material, MaterialVariantId materialId, int color, ShaderProvider.Armor shaderProvider) {
        if (shaderProvider != null && Config.USE_SHADER.get()) {
            return new TintedShaderArmorTexture(material, color, shaderProvider, materialId);
        }
        return texture;
    }

    @Unique
    private static ResourceLocation ticex$getAtlasLocation(ResourceLocation name) {
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "tinker_armor/" + name.getPath());
    }
}