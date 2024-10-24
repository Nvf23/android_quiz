package com.example.quizactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {
    private TextView mQuestionTextView;
    private TextView mResultsTextView;  // TextView para mostrar los resultados
    private final Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;
    private boolean[] mAnswersGiven;

    private ImageView[] mPoints;

    private Button mTrueButton;
    private Button mFalseButton;
    private int mCorrectAnswers = 0;
    private int mIncorrectAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestionTextView = findViewById(R.id.question_text_view);
        mResultsTextView = findViewById(R.id.results_text_view);  // Inicializamos el TextView de resultados

        mPoints = new ImageView[]{
                findViewById(R.id.point_1),
                findViewById(R.id.point_2),
                findViewById(R.id.point_3),
                findViewById(R.id.point_4),
                findViewById(R.id.point_5),
                findViewById(R.id.point_6)
        };

        mAnswersGiven = new boolean[mQuestionBank.length];

        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        Button mNextButton = findViewById(R.id.next_button);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt("currentIndex");
            mAnswersGiven = savedInstanceState.getBooleanArray("answersGiven");
            mCorrectAnswers = savedInstanceState.getInt("correctAnswers");
            mIncorrectAnswers = savedInstanceState.getInt("incorrectAnswers");
            String resultsText = savedInstanceState.getString("resultsText");

            updatePoints();

            if (mAnswersGiven[mCurrentIndex]) {
                disableAnswerButtons();
            }

            if (resultsText != null) {
                mResultsTextView.setText(resultsText);
                mResultsTextView.setVisibility(View.VISIBLE);  // Asegúrate de que el TextView sea visible
            }
        }

        mTrueButton.setOnClickListener(v -> {
            checkAnswer(true);
            disableAnswerButtons();
        });

        mFalseButton.setOnClickListener(v -> {
            checkAnswer(false);
            disableAnswerButtons();
        });

        mNextButton.setOnClickListener(v -> {
            if (mCurrentIndex < mQuestionBank.length - 1) {
                mCurrentIndex++;
                updateQuestion();
                if (!mAnswersGiven[mCurrentIndex]) {
                    enableAnswerButtons();
                }
            } else {
                showResults();
                mNextButton.setEnabled(false);
            }
        });

        updateQuestion();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentIndex", mCurrentIndex);
        outState.putBooleanArray("answersGiven", mAnswersGiven);
        outState.putInt("correctAnswers", mCorrectAnswers);
        outState.putInt("incorrectAnswers", mIncorrectAnswers);
        outState.putString("resultsText", mResultsTextView.getText().toString()); // Guardar el texto de resultados
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId;

        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            mPoints[mCurrentIndex].setBackgroundResource(R.drawable.circle_correct);
            mCorrectAnswers++;
        } else {
            messageResId = R.string.incorrect_toast;
            mPoints[mCurrentIndex].setBackgroundResource(R.drawable.circle_incorrect);
            mIncorrectAnswers++;
        }

        mAnswersGiven[mCurrentIndex] = true;
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    private void disableAnswerButtons() {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    private void enableAnswerButtons() {
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    private void updatePoints() {
        for (int i = 0; i < mAnswersGiven.length; i++) {
            if (mAnswersGiven[i]) {
                if (mQuestionBank[i].isAnswerTrue()) {
                    mPoints[i].setBackgroundResource(R.drawable.circle_correct);
                } else {
                    mPoints[i].setBackgroundResource(R.drawable.circle_incorrect);
                }
            }
        }
    }

    private void showResults() {
        int totalQuestions = mQuestionBank.length;
        int correctPercentage = (mCorrectAnswers * 100) / totalQuestions;
        String resultMessage = getString(R.string.results_message, mCorrectAnswers, mIncorrectAnswers, correctPercentage);

        mResultsTextView.setText(resultMessage);
        mResultsTextView.setVisibility(View.VISIBLE);  // Hacemos visible el TextView de resultados
    }
}