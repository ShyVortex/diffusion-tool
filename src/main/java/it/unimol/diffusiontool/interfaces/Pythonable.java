package it.unimol.diffusiontool.interfaces;

import it.unimol.diffusiontool.exceptions.GenerationException;
import it.unimol.diffusiontool.exceptions.UpscalingException;
import javafx.scene.image.Image;

import java.io.IOException;

public interface Pythonable {
    String callPythonScript(String str1, String str2, Image img) throws IOException, GenerationException,
            UpscalingException;
    String callPythonScript(String str1, String str2) throws IOException, GenerationException;
    String callPythonScript(Image img) throws IOException, UpscalingException;
}
