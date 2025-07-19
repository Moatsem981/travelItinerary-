package com.example.travelappcw;

import android.app.Activity;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.TranslateLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SpeakTranslateFragment extends Fragment {

    // UI components
    private Spinner sourceLanguageSpinner, targetLanguageSpinner; // Dropdowns for selecting source and target languages
    private EditText speechInputText; // show recognized speech as editable text
    private TextView translatedText;  // shows the translated result
    private ImageButton micButton;    // that is the Button to start speech recognition
    private Button translateButton;   // that Button to trigger translation

    // these are the Core functionality components
    private SpeechRecognizer speechRecognizer; // this is Used to recognize speech input
    private Translator translator;             // ML Kit translator which recognises text

    public SpeakTranslateFragment() {
        // Required empty public constructor
    }

    // This method sets up the UI and event listeners
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_speaktranslate, container, false);

        // Link the XML layout components to Java objects
        sourceLanguageSpinner = view.findViewById(R.id.sourceLanguageSpinner);
        targetLanguageSpinner = view.findViewById(R.id.targetLanguageSpinner);
        speechInputText = view.findViewById(R.id.speechInputText);
        translatedText = view.findViewById(R.id.translatedText);
        micButton = view.findViewById(R.id.micButton);
        translateButton = view.findViewById(R.id.translateButton);

        // Load language options into the dropdowns
        setupLanguageSpinners();

        // Initialize the Android speech recognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());

        // Start speech recognition when the mic button is clicked
        micButton.setOnClickListener(v -> startSpeechRecognition());

        // Start translation when the translate button is clicked
        translateButton.setOnClickListener(v -> translateText());

        return view;
    }

    // This method sets up the language dropdowns with supported language options
    private void setupLanguageSpinners() {
        // Languages supported by the app
        String[] languages = {"English", "French", "Spanish", "German", "Chinese", "Japanese"};

        // Set up the adapter to show the language options in both spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, languages);
        sourceLanguageSpinner.setAdapter(adapter);
        targetLanguageSpinner.setAdapter(adapter);
    }

    // This method starts the Android speech recognition intent
    private void startSpeechRecognition() {
        // Create a speech recognition intent
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Set the language model to free form (natural speech)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Set the default locale (device language)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        // Display prompt to user
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

        // Start the activity and wait for result (code 100)
        startActivityForResult(intent, 100);
    }

    // This method handles the result from the speech recognizer
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from our speech request and is successful
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            // Get recognized speech results
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            // If results exist, set the first one as the input text
            if (result != null && !result.isEmpty()) {
                speechInputText.setText(result.get(0));
            }
        }
    }

    // This method handles translation of text using Google ML Kit
    private void translateText() {
        // Get the text to be translated from the EditText
        String textToTranslate = speechInputText.getText().toString();

        // If no text entered, show a warning
        if (textToTranslate.isEmpty()) {
            Toast.makeText(getContext(), "Please enter text to translate", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the selected source and target languages as ML Kit codes
        String sourceLang = getLanguageCode(sourceLanguageSpinner.getSelectedItem().toString());
        String targetLang = getLanguageCode(targetLanguageSpinner.getSelectedItem().toString());

        // Set up the translation options
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(sourceLang)
                        .setTargetLanguage(targetLang)
                        .build();

        // Create a Translator instance
        translator = Translation.getClient(options);

        // Download the language model if needed, then translate the text
        translator.downloadModelIfNeeded()
                .addOnSuccessListener(unused -> {
                    // Translate the input text
                    translator.translate(textToTranslate)
                            .addOnSuccessListener(translated -> translatedText.setText(translated)) // Show translated result
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(), "Translation failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Model download failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    // Helper method to convert language names to ML Kit language codes
    private String getLanguageCode(String language) {
        // Map of supported languages
        Map<String, String> languageMap = new HashMap<>();
        languageMap.put("English", TranslateLanguage.ENGLISH);
        languageMap.put("French", TranslateLanguage.FRENCH);
        languageMap.put("Spanish", TranslateLanguage.SPANISH);
        languageMap.put("German", TranslateLanguage.GERMAN);
        languageMap.put("Chinese", TranslateLanguage.CHINESE);
        languageMap.put("Japanese", TranslateLanguage.JAPANESE);

        // Return the ML Kit code or default to English if language not found
        return languageMap.getOrDefault(language, TranslateLanguage.ENGLISH);
    }
}
