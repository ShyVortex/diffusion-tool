package it.unimol.diffusiontool.interfaces;

import it.unimol.diffusiontool.exceptions.GenerationException;
import it.unimol.diffusiontool.exceptions.UpscalingException;

import java.io.File;
import java.io.IOException;

public interface Pythonable {
    String callPythonScript(String prompt, String tags, String date, String img) throws IOException,
            GenerationException, UpscalingException;
    String callPythonScript(String prompt, String tags, String date) throws IOException, GenerationException;
    String callPythonScript(String img) throws IOException, UpscalingException;
    File findPythonVenv();
    File findPythonScript();
}
