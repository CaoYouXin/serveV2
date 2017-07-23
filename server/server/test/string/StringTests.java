package string;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import rest.JsonResponse;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StringTests {

    @Test
    void testFileSep() {
        System.out.println(File.separator);
    }

    @Test
    void replaceAllTest() {
        System.out.println("?to=".replaceAll("\\?", "\\\\?"));
    }

    @Test
    void regTest() {
        Pattern compile1 = Pattern.compile("^/metaApi/controller/set/(?<className>.*?)$");
        Pattern compile2 = Pattern.compile("^/metaApi/controller/set/(?<id>.*?)/(?<disabled>.*?)$");
        Pattern compile3 = Pattern.compile("^/metaApi/controller/set/(?<className>[^\\/\\?&]*?)$");
        Pattern compile4 = Pattern.compile("^/metaApi/controller/set/(?<id>.*?)/(?<disabled>[^\\/\\?&]*?)$");
        Pattern compile5 = Pattern.compile("/metaApi/controller/set/(?<className>[^\\/\\?&]*?)");
        Pattern compile6 = Pattern.compile("/metaApi/controller/set/(?<id>.*?)/(?<disabled>[^\\/\\?&]*?)");

        String url = "/metaApi/controller/set/1/T";

        System.out.println(compile1.matcher(url).matches());
        System.out.println(compile2.matcher(url).matches());
        System.out.println(compile3.matcher(url).matches());
        System.out.println(compile4.matcher(url).matches());
        System.out.println(compile5.matcher(url).matches());
        System.out.println(compile6.matcher(url).matches());
    }

    @Test
    void testJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonResponse jsonResponse = null;
        try {
            jsonResponse = objectMapper.readValue("{\"code\":20000, \"body\":{\"key\":\"value\"}}", JsonResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(jsonResponse);
        assertEquals(20000, jsonResponse.getCode());
        System.out.println(jsonResponse.getBody());
    }

}
