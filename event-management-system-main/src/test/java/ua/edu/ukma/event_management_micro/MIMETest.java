package ua.edu.ukma.event_management_micro;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class MIMETest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetUserJson() throws Exception {

        MvcResult result = this.mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.createUser().toString()))
                .andExpect(status().isCreated())
                .andReturn();


        String location = "/" + result.getResponse().getHeader("Location");
        location = this.injectPathSegment(location, "json");

        this.mockMvc.perform(get(location)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserHTML() throws Exception {

        MvcResult result = this.mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.createUser().toString()))
                .andExpect(status().isCreated())
                .andReturn();

        String location = "/" + result.getResponse().getHeader("Location");
        location = this.injectPathSegment(location, "html");

        this.mockMvc.perform(get(location)
                .accept(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @Test
    public void testGenLogoPNG() throws Exception {
        String location = "/api/static/icon";
        this.mockMvc.perform(get(location)
                .accept(MediaType.IMAGE_PNG))
                .andDo(print())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"icon.png\""))
                .andExpect(status().isOk());
    }

    private JSONObject createUser() throws Exception {
        JSONObject user = new JSONObject();

        user.put("userRole", "USER");
        user.put("username", "O_0lenka");
        user.put("firstName", "Olena");
        user.put("lastName", "Petrenko");
        user.put("email", "olena.petrenko@example.com");
        user.put("password", "SecurePass123");
        user.put("phoneNumber", "+380931112233");
        user.put("dateOfBirth", "1990-05-14");
        return user;
    }

    private String injectPathSegment(String location, String segment) {
        String[] parts = location.split("/");
        ArrayList<String> list = new ArrayList<>(parts.length);
        Collections.addAll(list, parts);

        int pos = list.size() - 1;
        list.add(pos, segment);
        return String.join("/", list);
    }
}
