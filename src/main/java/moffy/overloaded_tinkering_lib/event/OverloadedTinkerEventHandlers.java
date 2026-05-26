package moffy.overloaded_tinkering_lib.event;

import moffy.overloaded_tinkering_lib.client.model.MaterialOverrideModel;
import net.minecraftforge.client.event.ModelEvent;

public class OverloadedTinkerEventHandlers {


    public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register("mat_override_obj", MaterialOverrideModel.LOADER);
    }
}
