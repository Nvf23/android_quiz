package com.example.quizactivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {
    private TextView mQuestionTextView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestionTextView = findViewById(R.id.question_text_view);

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

            updatePoints();

            if (mAnswersGiven[mCurrentIndex]) {
                disableAnswerButtons();
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
        } else {
            messageResId = R.string.incorrect_toast;
            mPoints[mCurrentIndex].setBackgroundResource(R.drawable.circle_incorrect);
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
}