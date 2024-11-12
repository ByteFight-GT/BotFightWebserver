package com.example.botfightwebserver.storage;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class storageTest {

    // This is a bad test. Ignore until we figure out where to put it.
    @Test
    void testStorageAccess() {
        // Arrange
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // Act
        Bucket bucket = storage.get("botfight_submissions");

        // Assert
        assertNotNull(bucket, "Bucket should exist");
        assertEquals("botfight_submissions", bucket.getName(), "Bucket name should match");

        // Optional: Test if we can list files
        assertTrue(bucket.list().iterateAll().iterator().hasNext(), "Bucket should be accessible");
    }
}
