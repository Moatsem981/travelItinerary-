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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SpeakTranslateFragment extends Fragment {

    private Spinner sourceLanguageSpinner, targetLanguageSpinner;
    private EditText speechInputText;
    private TextView translatedText;
    private ImageButton micButton;
    private Button translateButton;

    private SpeechRecognizer speechRecognizer;
    private Translator translator;

    public SpeakTranslateFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speaktranslate, container, false);

        sourceLanguageSpinner = view.findViewById(R.id.sourceLanguageSpinner);
        targetLanguageSpinner = view.findViewById(R.id.targetLanguageSpinner);
        speechInputText = view.findViewById(R.id.speechInputText);
        translatedText = view.findViewById(R.id.translatedText);
        micButton = view.findViewById(R.id.micButton);
        translateButton = view.findViewById(R.id.translateButton);

        setupLanguageSpinners();

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());

        micButton.setOnClickListener(v -> startSpeechRecognition());

        translateButton.setOnClickListener(v -> translateText());

        return view;
    }


    private void setupLanguageSpinners() {
        String[] languages = {"English", "French", "Spanish", "German", "Chinese", "Japanese"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, languages);
        sourceLanguageSpinner.setAdapter(adapter);
        targetLanguageSpinner.setAdapter(adapter);
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                speechInputText.setText(result.get(0));
            }
        }
    }


    private void translateText() {
        String textToTranslate = speechInputText.getText().toString();
        if (textToTranslate.isEmpty()) {
            Toast.makeText(getContext(), "Please enter text to translate", Toast.LENGTH_SHORT).show();
            return;
        }

        String sourceLang = getLanguageCode(sourceLanguageSpinner.getSelectedItem().toString());
        String targetLang = getLanguageCode(targetLanguageSpinner.getSelectedItem().toString());

        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(sourceLang)
                        .setTargetLanguage(targetLang)
                        .build();

        translator = Translation.getClient(options);

        translator.downloadModelIfNeeded()
                .addOnSuccessListener(unused -> {
                    translator.translate(textToTranslate)
                            .addOnSuccessListener(translated -> translatedText.setText(translated))
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(), "Translation failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Model download failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private String getLanguageCode(String language) {
        Map<String, String> languageMap = new HashMap<>();
        languageMap.put("English", TranslateLanguage.ENGLISH);
        languageMap.put("French", TranslateLanguage.FRENCH);
        languageMap.put("Spanish", TranslateLanguage.SPANISH);
        languageMap.put("German", TranslateLanguage.GERMAN);
        languageMap.put("Chinese", TranslateLanguage.CHINESE);
        languageMap.put("Japanese", TranslateLanguage.JAPANESE);

        return languageMap.getOrDefault(language, TranslateLanguage.ENGLISH); // Default to English
    }
}
