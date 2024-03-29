import it.unimol.diffusiontool.exceptions.UpscalingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("JUnit not compatible with Python calls")

@DisplayName("Test for image upscaling")
public class UpscalingTest {
    private String prompt;
    private String tags;
    private final String pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
    private final Pattern regex = Pattern.compile(pattern);

    @BeforeEach
    public void init() {
        prompt = "a photo of an astronaut riding a horse on mars";
        tags = "";
    }

    @Test
    public void mainTest() {
        String encodedImage = "";

        try {
            encodedImage = callPythonScript(prompt, tags);
        } catch (IOException | UpscalingException e) {
            e.printStackTrace();
        }
        assertNotNull(encodedImage);

        Matcher b64check = regex.matcher(encodedImage);
        assertTrue(b64check.find());
    }

    @DisplayName("Upscaling")
    public String callPythonScript(String prompt, String tags) throws IOException, UpscalingException {
        // Get all necessary paths, initialize log
        File pythonVenv = findPythonVenv();
        String activateScriptPath = pythonVenv.getPath();
        File pythonScript = findPythonScript();
        String pythonScriptPath = pythonScript.getPath();
        StringBuilder output = new StringBuilder();

        // Construct the command to execute
        List<String> upscaleCommand;
        ProcessBuilder processBuilder;
        upscaleCommand = List.of (
                activateScriptPath,      // Activate virtual environment
                pythonScriptPath,        // Path to the Python script
                prompt,                  // Prompt argument
                tags                   // Tags argument
        );
        processBuilder = new ProcessBuilder(upscaleCommand);

        // Start the process, capture input stream
        Process process = processBuilder.start();
        try (
                InputStream inputStream = process.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader inputbufferedReader = new BufferedReader(inputStreamReader)
        ) {
            String inputLine;
            while ((inputLine = inputbufferedReader.readLine()) != null) {
                output.append(inputLine).append("\n");
            }
        }

        // Wait for the process to finish
        try {
            int exitCode = process.waitFor();
            System.out.println("\nPython script exited with code: " + exitCode);

            if (exitCode == 0) {
                // If there is no error, return the output as a String
                return output.toString().trim();
            } else
                throw new UpscalingException();

        } catch (InterruptedException e) {
            throw new UpscalingException();
        }
    }

    public File findPythonVenv() {
        // Get user's home directory and virtual environment folder
        String userHome = System.getProperty("user.home");
        File directory = new File(userHome, "venv");

        return searchFile(directory, "python");
    }

    public File findPythonScript() {
        // Get the working directory
        String workingDirectory = System.getProperty("user.dir");
        File directory = new File(workingDirectory);
        String fileName = "upscale_test.py";

        return searchFile(directory, fileName);
    }

    private File searchFile(File directory, String fileName) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively search in subdirectories
                    File foundScript = searchFile(file, fileName);
                    if (foundScript != null)
                        return foundScript;
                } else if (Objects.equals(file.getName(), fileName))
                    return file;
            }
        }

        return null;
    }
}
