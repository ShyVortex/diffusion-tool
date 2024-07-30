import it.unimol.diffusiontool.exceptions.GenerationException;
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

@DisplayName("Test for image generation")
public class GenerationTest {
    private String prompt;
    private String tags;
    private static int iteration;
    private final String pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
    private final Pattern regex = Pattern.compile(pattern);

    @BeforeEach
    public void init() {
        prompt = "a photo of an astronaut riding a horse on mars";
        tags = "";
        iteration = 1;
    }

    @Test
    public void mainTest() {
        String encodedImage = "";

        // stable-diffusion-2-1 test
        try {
            encodedImage = callPythonScript(prompt, tags);
        } catch (IOException | GenerationException e) {
            e.printStackTrace();
        }
        assertNotNull(encodedImage);

        Matcher b64check = regex.matcher(encodedImage);
        assertTrue(b64check.find());
        iteration++;

        // stable-diffusion-3 test
        try {
            encodedImage = callPythonScript(prompt, tags);
        } catch (IOException | GenerationException e) {
            e.printStackTrace();
        }
        assertNotNull(encodedImage);

        Matcher b64check2 = regex.matcher(encodedImage);
        assertTrue(b64check2.find());
        iteration++;

        // pixel-art-style test
        try {
            encodedImage = callPythonScript(prompt, tags);
        } catch (IOException | GenerationException e) {
            e.printStackTrace();
        }
        assertNotNull(encodedImage);

        Matcher b64check3 = regex.matcher(encodedImage);
        assertTrue(b64check3.find());
    }

    @DisplayName("Generation")
    public String callPythonScript(String prompt, String tags) throws IOException, GenerationException {
        // Get all necessary paths, initialize log
        File pythonVenv = findPythonVenv();
        String activateScriptPath = pythonVenv.getPath();
        File pythonScript = findPythonScript(iteration);
        String pythonScriptPath = pythonScript.getPath();
        StringBuilder output = new StringBuilder();

        switch (iteration) {
            case 1: // call stable-diffusion-2-1 pipeline
                prompt = prompt + ", sd2-1";
                break;
            case 2: // call stable-diffusion-3 pipeline
                prompt = prompt + ", sd3";
                break;
            case 3: // call pixel-art-style pipeline
                prompt = prompt + ", pixel-art-style";
                break;
            default:
                throw new UnsupportedOperationException();
        }

        // Construct the command to execute
        List<String> generateCommand;
        ProcessBuilder processBuilder;
        generateCommand = List.of (
                    activateScriptPath,      // Activate virtual environment
                    pythonScriptPath,        // Path to the Python script
                    prompt,                  // Prompt argument
                    tags                   // Tags argument
            );
        processBuilder = new ProcessBuilder(generateCommand);

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
                throw new GenerationException();

        } catch (InterruptedException e) {
            throw new GenerationException();
        }
    }

    public File findPythonVenv() {
        // Get user's home directory and virtual environment folder
        String userHome = System.getProperty("user.home");
        File directory = new File(userHome, "venv");

        return searchFile(directory, "python");
    }

    public File findPythonScript(int iteration) {
        // Get the working directory
        String workingDirectory = System.getProperty("user.dir");
        File directory = new File(workingDirectory);

        // Find appropriate script
        String fileName = switch (iteration) {
            case 1 -> "generate_sd2-1_test.py";
            case 2 -> "generate_sd3_test.py";
            case 3 -> "generate_pixart_test.py";
            default -> throw new UnsupportedOperationException();
        };

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
