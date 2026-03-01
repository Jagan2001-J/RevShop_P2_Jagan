package com.rev.app.rest;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PagedResponseTest {

    @Test
    void testPagedResponse_Creation() {
        List<String> content = Arrays.asList("Item 1", "Item 2");
        PagedResponse<String> response = new PagedResponse<>(content, 0, 10, 2, 1, true);

        assertEquals(content, response.getContent());
        assertEquals(0, response.getPageNumber());
        assertEquals(10, response.getPageSize());
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertTrue(response.isLast());
    }

    @Test
    void testPagedResponse_NoArgsConstructor() {
        PagedResponse<Object> response = new PagedResponse<>();
        assertNotNull(response);
    }

    @Test
    void testPagedResponse_SettersAndGetters() {
        PagedResponse<Integer> response = new PagedResponse<>();
        List<Integer> content = Arrays.asList(1, 2, 3);

        response.setContent(content);
        response.setPageNumber(1);
        response.setPageSize(5);
        response.setTotalElements(15);
        response.setTotalPages(3);
        response.setLast(false);

        assertEquals(content, response.getContent());
        assertEquals(1, response.getPageNumber());
        assertEquals(5, response.getPageSize());
        assertEquals(15, response.getTotalElements());
        assertEquals(3, response.getTotalPages());
        assertFalse(response.isLast());
    }
}
