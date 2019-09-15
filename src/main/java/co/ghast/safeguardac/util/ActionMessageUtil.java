package co.ghast.safeguardac.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionMessageUtil {

    private List<AMText> Text = new ArrayList<>();

    public enum ClickableType {
        RunCommand("run_command"),
        SuggestCommand("suggest_command"),
        OpenURL("open_url");

        public String Action;

        ClickableType(String Action) {
            this.Action = Action;
        }
    }

    public class AMText {

        private String Message = "";

        private Map<String, Map.Entry<String, String>> Modifiers = new HashMap<String, Map.Entry<String, String>>();

        public AMText(String Text) {
            this.Message = Text;
        }

        public String getMessage() {
            return this.Message;
        }

        public String getFormattedMessage() {
            String Chat = "{\"text\":\"" + this.Message + "\"";
            for (String Event : this.Modifiers.keySet()) {
                Map.Entry<String, String> Modifier = this.Modifiers.get(Event);
                Chat += ",\"" + Event + "\":{\"action\":\"" + Modifier.getKey() + "\",\"value\":" + Modifier.getValue() + "}";
            }
            Chat += "}";
            return Chat;
        }

        public AMText addHoverText(String... Text) {
            String Event = "hoverEvent";
            String Key = "show_text";
            String Value = "";
            if (Text.length == 1)
                Value = "{\"text\":\"" + Text[0] + "\"}";
            else {
                Value = "{\"text\":\"\",\"extra\":[";
                for (String Message : Text)
                    Value += "{\"text\":\"" + Message + "\"},";
                Value = Value.substring(0, Value.length() - 1);
                Value += "]}";
            }
            Map.Entry<String, String> Values = new AbstractMap.SimpleEntry<String, String>(Key, Value);
            this.Modifiers.put(Event, Values);
            return this;
        }

        public AMText addHoverItem(ItemStack Item) {
            try {
                String Event = "hoverEvent";
                String Key = "show_item";
                Object cfi = ReflectionUtil.getCraftBukkitClass("CraftItemStack");
                Object nmscopy = cfi.getClass().getMethod("asNMSCopy", Item.getClass()).invoke(Item);
                String Value = (String) nmscopy.getClass().getMethod("getTag").invoke(nmscopy);
                Map.Entry<String, String> Values = new AbstractMap.SimpleEntry<String, String>(Key, Value);
                this.Modifiers.put(Event, Values);
                return this;
            } catch (Exception e) {
                e.printStackTrace();
                return this;
            }
        }

        public AMText setClickEvent(ClickableType Type, String Value) {
            String Event = "clickEvent";
            String Key = Type.Action;
            Map.Entry<String, String> Values = new AbstractMap.SimpleEntry<String, String>(Key, "\"" + Value + "\"");
            this.Modifiers.put(Event, Values);
            return this;
        }

    }

    public AMText addText(String Message) {
        AMText Text = new AMText(Message);
        this.Text.add(Text);
        return Text;
    }

    public String getFormattedMessage() {
        String Chat = "[\"\",";
        for (AMText Text : this.Text)
            Chat += Text.getFormattedMessage() + ",";
        Chat = Chat.substring(0, Chat.length() - 1);
        Chat += "]";
        return Chat;
    }

    public void sendToPlayer(Player Player) {
        try {
            Method serializer = ReflectionUtil.getNMSClass("ChatSerializer").getMethod(
                    "a", String.class);
            Object base = serializer.invoke(serializer, this.getFormattedMessage());
            Constructor packetConstructor = ReflectionUtil.getNMSClass("PacketPlayOutChat")
                    .getConstructor(ReflectionUtil.getNMSClass("IChatBaseComponent"), int.class);
            Object packet = packetConstructor.newInstance(base, 1);

            Object handle = ReflectionUtil.getPlayerHandle(Player);

            if (handle != null) {
                Object connection = handle.getClass().getField("playerConnection").get(handle);
                connection.getClass().getMethod("sendPacket", ReflectionUtil.getNMSClass(
                        "Packet")).invoke(connection, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
