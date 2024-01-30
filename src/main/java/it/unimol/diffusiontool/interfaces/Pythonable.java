package it.unimol.diffusiontool.interfaces;

import it.unimol.diffusiontool.exceptions.GenerationException;
import it.unimol.diffusiontool.exceptions.UpscalingException;

import java.io.File;
import java.io.IOException;

public interface Pythonable {
    String callPyScript(String prompt, String tags, String date, String path) throws IOException,
            GenerationException, UpscalingException;
    String callPyScript(String prompt, String tags, String date) throws IOException, GenerationException;
    String callPyScript(String img) throws IOException, UpscalingException;
    File findPyVenv();
    File findPyScript();
}
