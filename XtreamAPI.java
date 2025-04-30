import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Based off of chazlarson's py-xtream-codes
// 
//  https://github.com/chazlarson/py-xtream-codes


public class XtreamAPI {

    private String server;
    private String username;
    private String password;

    public static String LIVE_TYPE = "Live";
    public static final String VOD_TYPE = "VOD";
    public static final String SERIES_TYPE = "Series";

    public int num;
    public String name;
    public String stream_type;
    public int stream_id = 2043865;
    public String epg_channel_id;
    public String category_id;

    public XtreamAPI(String server, String username, String password) {
        this.server = server;
        this.username = username;
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthenticateURL() {
        return String.format("%s/player_api.php?username=%s&password=%s", server, username, password);
    }

    public String authenticate() {
        try {
            String authenticateURL = getAuthenticateURL();
            HttpURLConnection connection = (HttpURLConnection) new URL(authenticateURL).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String retrieveStreamJson()
    {
        try {
            String authenticateURL = getLiveStreamsURL();
            HttpURLConnection connection = (HttpURLConnection) new URL(authenticateURL).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void retrieveStreamInformation()
    {
        JSONArray jsonArray = new JSONArray(retrieveStreamJson());

        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject obj = jsonArray.getJSONObject(i);

            num = obj.getInt("num");
            name = obj.getString("name");
            stream_type = obj.getString("stream_type");
            stream_id = obj.getInt("stream_id");
            epg_channel_id = obj.isNull("epg_channel_id") ? "N/A" : obj.getString("epg_channel_id");
            category_id = obj.getString("category_id");

            System.out.println(num + " " + name + " " + stream_type + " " + stream_id + " " + epg_channel_id + " " + category_id);


        }

    }

    public String categories(String streamType) {
        String theURL = "";
        if (streamType.equals(LIVE_TYPE)) {
            theURL = getLiveCategoriesURL();
        } else if (streamType.equals(VOD_TYPE)) {
            theURL = getVodCategoriesURL();
        } else if (streamType.equals(SERIES_TYPE)) {
            theURL = getSeriesCategoriesURL();
        }
        return theURL;
    }


    public String streams(String streamType) {
        String theURL = "";
        if (streamType.equals(LIVE_TYPE)) {
            theURL = getLiveStreamsURL();
        } else if (streamType.equals(VOD_TYPE)) {
            theURL = getVodStreamsURL();
        } else if (streamType.equals(SERIES_TYPE)) {
            theURL = getSeriesURL();
        }
        return theURL;
    }


    public String getLiveCategoriesURL() {
        return String.format("%s/player_api.php?username=%s&password=%s&action=get_live_categories", server, username, password);
    }


    public String getVodCategoriesURL() {
        return String.format("%s/player_api.php?username=%s&password=%s&action=get_vod_categories", server, username, password);
    }


    public String getSeriesCategoriesURL() {
        return String.format("%s/player_api.php?username=%s&password=%s&action=get_series_categories", server, username, password);
    }


    public String getLiveStreamsURL() {
        return String.format("%s/player_api.php?username=%s&password=%s&action=get_live_streams", server, username, password);
    }



    public String getVodStreamsURL() {
        return String.format("%s/player_api.php?username=%s&password=%s&action=get_vod_streams", server, username, password);
    }


    public String getSeriesURL() {
        return String.format("%s/player_api.php?username=%s&password=%s&action=get_series", server, username, password);
    }

    public String get_all_epg_URL()
    {
        return String.format("%s/xmltv.php?username=%s&password=%s", server, username,password);
    }

    public String get_playable_vod_url()
    {
        return String.format("%s/movie/%s/%s/%s.mkv", server, username, password, stream_id);
    }

    public String get_playable_live_url()
    {
        return String.format("%s/live/%s/%s/%s.m3u8", server, username, password, stream_id);
    }

    public String get_vod_info()
    {
        return String.format("%s/player_api.php?username=%s&password=%s&action=get_vod_info&vod_id=%s", server, username, password, stream_id);
    }

    
}