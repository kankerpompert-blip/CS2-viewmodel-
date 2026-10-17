package dev.cs2view;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.RotationAxis;
import org.lwjgl.glfw.GLFW;

public class CS2ViewModel implements ClientModInitializer {
    // ---- tweak these ----
    public static float OFFSET_X = 0.0f, OFFSET_Y = 0.0f, OFFSET_Z = 0.0f;   // extra viewmodel offset (blocks)
    public static float SWAY_AMOUNT = 0.004f;                                 // idle "breathing" motion
    public static long  INSPECT_MS = 1700;                                    // inspect length
    // pivot of the inspect flip, in camera space (roughly where the right hand is)
    static final float PX = 0.45f, PY = -0.35f, PZ = -0.6f;

    private static KeyBinding inspectKey;
    private static long inspectStart = -1;

    @Override
    public void onInitializeClient() {
        KeyBinding.Category category = KeyBinding.Category.create(Identifier.of("cs2viewmodel", "main"));
        inspectKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cs2viewmodel.inspect", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F, category));
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (inspectKey.wasPressed()) inspectStart = Util.getMeasuringTimeMs();
        });
    }

    /** Called from the mixin right before the held sword is drawn. */
    public static void applyKnifeTransform(MatrixStack m) {
        long now = Util.getMeasuringTimeMs();
        double t = now / 1000.0;

        m.translate(OFFSET_X, OFFSET_Y, OFFSET_Z);
        // idle sway
        m.translate(Math.sin(t * 1.3) * SWAY_AMOUNT, Math.cos(t * 1.7) * SWAY_AMOUNT, 0);

        // inspect: lift, flip 360 around X, settle
        if (inspectStart >= 0) {
            float p = (now - inspectStart) / (float) INSPECT_MS;
            if (p >= 1f) { inspectStart = -1; return; }
            float ease = p < 0.5f ? 2 * p * p : 1 - (float) Math.pow(-2 * p + 2, 2) / 2;
            float lift = (float) Math.sin(p * Math.PI);
            m.translate(-0.12f * lift, 0.10f * lift, 0.05f * lift);
            m.translate(PX, PY, PZ);
            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-360f * ease));
            m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(25f * lift));
            m.translate(-PX, -PY, -PZ);
        }
    }
}
