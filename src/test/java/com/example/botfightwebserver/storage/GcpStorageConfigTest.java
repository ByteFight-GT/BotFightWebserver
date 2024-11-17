package com.example.botfightwebserver.storage;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class GcpStorageConfigTest {

    @Spy
    @InjectMocks
    private GcpStorageConfig gcpStorageConfig;

    private static final String TEST_CREDENTIALS_PATH = "test/path/credentials.json";
    private static final String TEST_PROJECT_ID = "test-project-id";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(gcpStorageConfig, "credentialsPath", TEST_CREDENTIALS_PATH);
        ReflectionTestUtils.setField(gcpStorageConfig, "projectId", TEST_PROJECT_ID);
    }

    @Test
    void shouldCreateStorageWithCredentials() throws IOException {
        FileInputStream mockFileInputStream = mock(FileInputStream.class);
        doReturn(mockFileInputStream).when(gcpStorageConfig).createFileInputStream(TEST_CREDENTIALS_PATH);

        try (MockedStatic<GoogleCredentials> mockedGoogleCreds = mockStatic(GoogleCredentials.class)) {
            GoogleCredentials mockCredentials = mock(GoogleCredentials.class);
            mockedGoogleCreds.when(() -> GoogleCredentials.fromStream(any(FileInputStream.class)))
                .thenReturn(mockCredentials);
            Storage storage = gcpStorageConfig.googleCloudStorage();
            assertNotNull(storage);

            assertEquals(mockCredentials, storage.getOptions().getCredentials());
            assertEquals(TEST_PROJECT_ID, storage.getOptions().getProjectId());
        }
    }

    @Test
    void shouldThrowException_WhenFileNotFound() throws IOException {
        doThrow(new IOException("File not found"))
            .when(gcpStorageConfig).createFileInputStream(TEST_CREDENTIALS_PATH);

        assertThrows(IOException.class, () -> gcpStorageConfig.googleCloudStorage());
    }
}