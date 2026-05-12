package me.logslow.util;

import org.bukkit.Material;

public class LogUtil {

    public static boolean isLog(Material material) {

        String name = material.name();

        return name.endsWith("_LOG")
                || name.endsWith("_WOOD")
                || name.equals("CRIMSON_STEM")
                || name.equals("WARPED_STEM");
    }
}
