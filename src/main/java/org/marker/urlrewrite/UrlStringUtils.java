package org.marker.urlrewrite;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by marker on 2017/6/26.
 */
public class UrlStringUtils {

    public static Map<String, String> parseQueryString(String s) {
        HashMap ht = new HashMap();
        StringTokenizer st = new StringTokenizer(s, "&");

        while(st.hasMoreTokens()) {
            String pair = st.nextToken();
            int pos = pair.indexOf(61);
            if(pos == -1) {
                ht.put(pair.toLowerCase(), "");
            } else {
                ht.put(pair.substring(0, pos).toLowerCase(), pair.substring(pos + 1));
            }
        }

        return ht;
    }
}
