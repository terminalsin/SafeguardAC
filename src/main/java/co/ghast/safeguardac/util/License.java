package co.ghast.safeguardac.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import co.ghast.safeguardac.SafeGuard;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class License {
    private String licenseKey;
    private SafeGuard instance = SafeGuard.getInstance();
    private Plugin plugin;
    private String mode;
    private String site = "0000// this is the website it is hosted on
    private String storecode = "0000// this is the storecode, it is used to validate the proper ownership of the website
    private String sku = "0000
    private int activation_id;
    public static FileConfiguration getConfig = SafeGuard.instance.getConfig();
    // checking if it's working with floobits. It is damnNNNNNN

    public License(String licenseKey, String mode, int activation_id, Plugin plugin) {
        this.licenseKey = licenseKey;
        this.plugin = plugin;
        this.mode = mode;
        this.activation_id = activation_id;

    }



    public static enum ValidationType{
        WRONG_RESPONSE, PAGE_ERROR, URL_ERROR, KEY_OUTDATED, KEY_NOT_FOUND, NOT_VALID_IP, INVALID_PLUGIN, VALID;
    }

    private void cout(String message){
        System.out.println(message);
    }
    private void cintout(int num){
        System.out.println(num);
    }


    public ValidationType isValid(){
        String license = licenseKey;
        String type = mode;
        String link = "";
        if (!getConfig.getBoolean("registered")) {
            link  = "https://" + site + "/wp-admin/admin-ajax.php?action="+ type + "&store_code=" + storecode +
                    "&sku=" + sku + "&license_key=" + license;
        } else {
            link  = "https://" + site + "/wp-admin/admin-ajax.php?action="+ type + "&store_code=" + storecode +
                    "&sku=" + sku + "&license_key=" + license + "&activation_id=" + activation_id;
        }
        try {



            int activation_id;
            String jsonS = "";
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while((inputLine = in.readLine()) != null) {
                jsonS+=inputLine;
            }


            Gson gson = new Gson();
            JsonObject jsonObject= gson.fromJson(jsonS, JsonObject.class);
            int id = jsonObject.get("status").getAsInt();
            if (!getConfig.getBoolean("registered")) {
                activation_id = jsonObject.get("data").getAsJsonObject().get("activation_id").getAsInt();
                //cintout(activation_id);
                //Utils.getConfig.set("activation_id", activation_id);
            }


            // DEBUG FUNCTIONS
        	/*cintout(id);
        	cout(link);*/

            try{

                if(id == 200) return ValidationType.VALID;
                else return ValidationType.WRONG_RESPONSE;
            } catch(IllegalArgumentException exc){

                if(id == 200) return ValidationType.VALID;
                else return ValidationType.WRONG_RESPONSE;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return ValidationType.VALID;
        }

    }

    public boolean checkLicense() {
        // ACTUAL ANTILEAK
        String license = licenseKey;
        String type = mode;

        cout("[]==========[License-System]==========[]");
        cout("Connecting to server... please be patient");
        instance.getLogger().info("License key: " + license);
        ValidationType vt = isValid();
        if (vt == ValidationType.VALID && !getConfig.getBoolean("registered")/*&& Utils.getConfig.getString("mode") == "license_key_activate"*/) {
            cout("License is valid, proceeding to next step");
            cout("License has been activated");
            cout("[]==========[License-System]==========[]");

			/*Utils.getConfig.set("registered", true);
			Utils.getConfig.set("mode", "license_key_validate");*/
            plugin.saveConfig();
            return true;

        } else if (vt == ValidationType.VALID && getConfig.getBoolean("registered")) {
            cout("License is valid, proceeding to next step");
            cout("License has been validated");
            cout("[]==========[License-System]==========[]");

            return true;

        } else {
            //cout (license);
            //cout (type);
            cout("License is invalid, shutting down the plugin");
            cout("[]==========[License-System]==========[]");
            Bukkit.getScheduler().cancelTasks(plugin);
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }

    }
}

