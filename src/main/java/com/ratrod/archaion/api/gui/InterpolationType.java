package com.ratrod.archaion.api.gui;

public enum InterpolationType {
    LINEAR, CATMULLROM,
    EASE_IN_SINE, EASE_OUT_SINE, EASE_IN_OUT_SINE,
    EASE_IN_QUAD, EASE_OUT_QUAD, EASE_IN_OUT_QUAD,
    EASE_IN_CUBIC, EASE_OUT_CUBIC, EASE_IN_OUT_CUBIC,
    EASE_IN_QUART, EASE_OUT_QUART, EASE_IN_OUT_QUART,
    EASE_IN_BACK, EASE_OUT_BACK, EASE_IN_OUT_BACK,
    EASE_IN_BOUNCE, EASE_OUT_BOUNCE, EASE_IN_OUT_BOUNCE;

    public static float apply(InterpolationType interpolation, float t) {
        return switch (interpolation) {
            case CATMULLROM -> {
                float t2 = t * t;
                float t3 = t2 * t;
                yield 0.5F * (2 * t2 + t3 - t);
            }
            case EASE_IN_SINE -> (float) (1 - Math.cos((t * Math.PI) / 2));
            case EASE_OUT_SINE -> (float) Math.sin((t * Math.PI) / 2);
            case EASE_IN_OUT_SINE -> (float) (-(Math.cos(Math.PI * t) - 1) / 2);
            case EASE_IN_QUAD -> t * t;
            case EASE_OUT_QUAD -> 1 - (1 - t) * (1 - t);
            case EASE_IN_OUT_QUAD -> t < 0.5 ? 2 * t * t : 1 - (float) Math.pow(-2 * t + 2, 2) / 2;
            case EASE_IN_CUBIC -> t * t * t;
            case EASE_OUT_CUBIC -> 1 - (float) Math.pow(1 - t, 3);
            case EASE_IN_OUT_CUBIC -> t < 0.5 ? 4 * t * t * t : 1 - (float) Math.pow(-2 * t + 2, 3) / 2;
            case EASE_IN_QUART -> t * t * t * t;
            case EASE_OUT_QUART -> 1 - (float) Math.pow(1 - t, 4);
            case EASE_IN_OUT_QUART -> t < 0.5 ? 8 * t * t * t * t : 1 - (float) Math.pow(-2 * t + 2, 4) / 2;
            case EASE_IN_BACK -> 2.70158F * t * t * t - 1.70158F * t * t;
            case EASE_OUT_BACK -> 1 + 2.70158F * (float)Math.pow(t - 1, 3) + 1.70158F * (float)Math.pow(t - 1, 2);
            case EASE_IN_OUT_BACK -> {
                float c1 = 1.70158F;
                float c2 = c1 * 1.525F;
                yield t < 0.5
                        ? ((float)Math.pow(2 * t, 2) * ((c2 + 1) * 2 * t - c2)) / 2
                        : ((float)Math.pow(2 * t - 2, 2) * ((c2 + 1) * (t * 2 - 2) + c2) + 2) / 2;
            }
            case EASE_IN_BOUNCE -> 1 - bounceOut(1 - t);
            case EASE_OUT_BOUNCE -> bounceOut(t);
            case EASE_IN_OUT_BOUNCE -> t < 0.5
                    ? (1 - bounceOut(1 - 2 * t)) / 2
                    : (1 + bounceOut(2 * t - 1)) / 2;

            default -> t;
        };
    }

    private static float bounceOut(float t) {
        float n1 = 7.5625F;
        float d1 = 2.75F;

        if (t < 1 / d1) {
            return n1 * t * t;
        } else if (t < 2 / d1) {
            return n1 * (t -= 1.5F / d1) * t + 0.75F;
        } else if (t < 2.5 / d1) {
            return n1 * (t -= 2.25F / d1) * t + 0.9375F;
        } else {
            return n1 * (t -= 2.625F / d1) * t + 0.984375F;
        }
    }

    public static float apply(int ordinal, float t) {
        InterpolationType[] values = InterpolationType.values();
        if (ordinal < 0 || ordinal >= values.length) return t;
        return apply(values[ordinal], t);
    }
}