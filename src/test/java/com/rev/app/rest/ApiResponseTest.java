package com.rev.app.rest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void testApiResponse_Success() {
        String data = "Test Data";
        ApiResponse<String> response = new ApiResponse<>(true, "Success Message", data);

        assertTrue(response.isSuccess());
        assertEquals("Success Message", response.getMessage());
        assertEquals(data, response.getData());
    }

    @Test
    void testApiResponse_Failure() {
        ApiResponse<Object> response = new ApiResponse<>(false, "Error Message", null);

        assertFalse(response.isSuccess());
        assertEquals("Error Message", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testApiResponse_NoArgsConstructor() {
        ApiResponse<String> response = new ApiResponse<>();
        assertNotNull(response);
    }

    @Test
    void testApiResponse_SettersAndGetters() {
        ApiResponse<Integer> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Custom Message");
        response.setData(123);

        assertTrue(response.isSuccess());
        assertEquals("Custom Message", response.getMessage());
        assertEquals(123, response.getData());
    }
}
