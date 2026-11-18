
import javax.swing.*;
import java.awt.*;

public class QuizApplication extends JFrame {

    String[] questions = {
        "Which keyword is used to create a class in Java?",
        "Which method is the starting point of a Java program?",
        "Which data type is used to store whole numbers?",
        "Which symbol is used to end a Java statement?",
        "Which keyword is used to inherit a class?"
    };

    String[][] options = {
        {"function", "class", "create", "object"},
        {"start()", "run()", "main()", "begin()"},
        {"float", "String", "int", "char"},
        {".", ":", ";", ","},
        {"this", "super", "extends", "implements"}
    };

    String[] answers = {
        "class", "main()", "int", ";", "extends"
    };

    String[] userAnswers = new String[questions.length];

    int currentQuestion = 0;
    int score = 0;
    int timeLeft = 15;

    JLabel questionLabel;
    JLabel timerLabel;
    JLabel progressLabel;
    JRadioButton[] radioButtons;
    ButtonGroup buttonGroup;
    JButton submitButton;

    Timer timer;

    public QuizApplication() {

        setTitle("Java Quiz Application");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        progressLabel = new JLabel("", SwingConstants.CENTER);
        timerLabel = new JLabel("", SwingConstants.CENTER);

        progressLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));

        topPanel.add(progressLabel);
        topPanel.add(timerLabel);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerPanel.add(questionLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        radioButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {

            radioButtons[i] = new JRadioButton();
            radioButtons[i].setFont(new Font("Arial", Font.PLAIN, 15));
            radioButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);

            buttonGroup.add(radioButtons[i]);
            centerPanel.add(radioButtons[i]);
            centerPanel.add(Box.createVerticalStrut(10));
        }

        add(centerPanel, BorderLayout.CENTER);

        submitButton = new JButton("Submit Answer");
        submitButton.setFont(new Font("Arial", Font.BOLD, 15));

        add(submitButton, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> submitAnswer());

        showQuestion();

        setVisible(true);
    }

    void showQuestion() {

        if (currentQuestion >= questions.length) {
            showResult();
            return;
        }

        progressLabel.setText(
            "Question " + (currentQuestion + 1)
            + " of " + questions.length
        );

        questionLabel.setText(questions[currentQuestion]);

        buttonGroup.clearSelection();

        for (int i = 0; i < 4; i++) {
            radioButtons[i].setText(options[currentQuestion][i]);
        }

        startTimer();
    }

    void startTimer() {

        if (timer != null) {
            timer.stop();
        }

        timeLeft = 15;
        timerLabel.setText("Time Left: " + timeLeft + " seconds");

        timer = new Timer(1000, e -> {

            timeLeft--;

            timerLabel.setText(
                "Time Left: " + timeLeft + " seconds"
            );

            if (timeLeft <= 0) {
                timer.stop();

                userAnswers[currentQuestion] = "Not Answered";

                JOptionPane.showMessageDialog(
                    this, "Time is up!"
                );

                currentQuestion++;
                showQuestion();
            }
        });

        timer.start();
    }

    void submitAnswer() {

        String selectedAnswer = null;

        for (JRadioButton button : radioButtons) {
            if (button.isSelected()) {
                selectedAnswer = button.getText();
                break;
            }
        }

        if (selectedAnswer == null) {
            JOptionPane.showMessageDialog(
                this, "Please select an answer!"
            );
            return;
        }

        timer.stop();

        userAnswers[currentQuestion] = selectedAnswer;

        if (selectedAnswer.equals(answers[currentQuestion])) {
            score++;
        }

        currentQuestion++;
        showQuestion();
    }

    void showResult() {

        if (timer != null) {
            timer.stop();
        }

        getContentPane().removeAll();

        JTextArea resultArea = new JTextArea();

        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        StringBuilder result = new StringBuilder();

        result.append("===== QUIZ RESULT =====\n\n");
        result.append("Final Score: ")
              .append(score).append("/")
              .append(questions.length).append("\n\n");

        for (int i = 0; i < questions.length; i++) {

            result.append("Q").append(i + 1).append(": ")
                  .append(questions[i]).append("\n");

            result.append("Your Answer: ")
                  .append(userAnswers[i]).append("\n");

            result.append("Correct Answer: ")
                  .append(answers[i]).append("\n");

            if (answers[i].equals(userAnswers[i])) {
                result.append("Result: Correct\n\n");
            } else {
                result.append("Result: Incorrect\n\n");
            }
        }

        resultArea.setText(result.toString());

        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        JButton closeButton = new JButton("Finish Quiz");

        closeButton.addActionListener(e -> dispose());

        add(closeButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new QuizApplication();
        });
    }
}
