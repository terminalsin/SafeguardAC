package co.ghast.safeguardac.util;

import org.bukkit.ChatColor;

public class ChatUtil {

    public static String Head = ChatColor.AQUA.toString();

    public static String Scramble = ChatColor.MAGIC.toString();
    public static String Bold = ChatColor.BOLD.toString();
    public static String Strike = ChatColor.STRIKETHROUGH.toString();
    public static String Line = ChatColor.UNDERLINE.toString();
    public static String Italics = ChatColor.ITALIC.toString();
    public static String Reset = ChatColor.WHITE.toString();

    public static String Aqua = ChatColor.AQUA.toString();
    public static String Black = ChatColor.BLACK.toString();
    public static String Blue = ChatColor.BLUE.toString();
    public static String DAqua = ChatColor.DARK_AQUA.toString();
    public static String DBlue = ChatColor.DARK_BLUE.toString();
    public static String DGray = ChatColor.DARK_GRAY.toString();
    public static String DGreen = ChatColor.DARK_GREEN.toString();
    public static String DPurple = ChatColor.DARK_PURPLE.toString();
    public static String DRed = ChatColor.DARK_RED.toString();
    public static String Gold = ChatColor.GOLD.toString();
    public static String Gray = ChatColor.GRAY.toString();
    public static String Green = ChatColor.GREEN.toString();
    public static String Purple = ChatColor.LIGHT_PURPLE.toString();
    public static String Red = ChatColor.RED.toString();
    public static String White = ChatColor.WHITE.toString();
    public static String Yellow = ChatColor.YELLOW.toString();

    public static String Split = "ï¿½";

    public static String strip(String Text) {
        return ChatColor.stripColor(Text);
    }

    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
