import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentManagementSystem {
    private final JFrame frame;
    private JTabbedPane tabbedPane;

    private java.util.List<Student> students = new ArrayList<>();
    private DefaultTableModel studentTableModel;

    private String[] courses = {"Mathematics", "Science", "History", "English"};

    public StudentManagementSystem() {
        frame = new JFrame("Student Management System");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();

        initStudentTab();
        initEnrollmentTab();
        initGradesTab();
        initViewStudentDetailsTab();

        frame.add(tabbedPane);

        frame.setVisible(true);
    }

    private void initStudentTab() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(3, 2));
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JButton addButton = new JButton("Add Student");

        form.add(new JLabel("Student Name:"));
        form.add(nameField);
        form.add(new JLabel("Student ID:"));
        form.add(idField);
        form.add(addButton);

        String[] columnNames = {"Name", "ID"};
        studentTableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(studentTableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();

            if (name.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields must be filled");
                return;
            }

            students.add(new Student(name, id));
            studentTableModel.addRow(new Object[]{name, id});
            nameField.setText("");
            idField.setText("");
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.add("Students", panel);
    }

    private void initEnrollmentTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JComboBox<String> courseBox = new JComboBox<>(courses);
        JComboBox<String> studentBox = new JComboBox<>();
        JButton enrollButton = new JButton("Enroll Student");

        JPanel top = new JPanel(new GridLayout(2, 2));
        top.add(new JLabel("Select Course:"));
        top.add(courseBox);
        top.add(new JLabel("Select Student:"));
        top.add(studentBox);

        panel.add(top, BorderLayout.NORTH);
        panel.add(enrollButton, BorderLayout.SOUTH);

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) {
                studentBox.removeAllItems();
                for (Student s : students) {
                    studentBox.addItem(s.getId() + " - " + s.getName());
                }
            }
        });

        enrollButton.addActionListener(e -> {
            int studentIndex = studentBox.getSelectedIndex();
            int courseIndex = courseBox.getSelectedIndex();

            if (studentIndex == -1 || courseIndex == -1) {
                JOptionPane.showMessageDialog(frame, "Please select both course and student");
                return;
            }

            Student student = students.get(studentIndex);
            student.enrollCourse(courses[courseIndex]);
            JOptionPane.showMessageDialog(frame, "Student enrolled successfully");
        });

        tabbedPane.add("Enrollment", panel);
    }

    private void initGradesTab() {
        JPanel panel = new JPanel(new BorderLayout());

        JComboBox<String> studentBox = new JComboBox<>();
        JComboBox<String> courseBox = new JComboBox<>();
        JTextField gradeField = new JTextField();
        JButton assignButton = new JButton("Assign Grade");

        JPanel top = new JPanel(new GridLayout(3, 2));
        top.add(new JLabel("Select Student:"));
        top.add(studentBox);
        top.add(new JLabel("Select Course:"));
        top.add(courseBox);
        top.add(new JLabel("Enter Grade:"));
        top.add(gradeField);

        panel.add(top, BorderLayout.NORTH);
        panel.add(assignButton, BorderLayout.SOUTH);

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 2) {
                studentBox.removeAllItems();
                for (Student s : students) {
                    studentBox.addItem(s.getId() + " - " + s.getName());
                }
            }
        });

        studentBox.addActionListener(e -> {
            courseBox.removeAllItems();
            int index = studentBox.getSelectedIndex();
            if (index >= 0) {
                Student student = students.get(index);
                for (String c : student.getCourses()) {
                    courseBox.addItem(c);
                }
            }
        });

        assignButton.addActionListener(e -> {
            int studentIndex = studentBox.getSelectedIndex();
            int courseIndex = courseBox.getSelectedIndex();
            String grade = gradeField.getText().trim();

            if (studentIndex == -1 || courseIndex == -1 || grade.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields");
                return;
            }

            Student student = students.get(studentIndex);
            student.assignGrade(courseBox.getSelectedItem().toString(), grade);
            JOptionPane.showMessageDialog(frame, "Grade assigned successfully");
        });

        tabbedPane.add("Grades", panel);
    }

    private void initViewStudentDetailsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Name", "ID", "Courses & Grades"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        JButton viewButton = new JButton("View Student Details");

        panel.add(viewButton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        viewButton.addActionListener(e -> {
            tableModel.setRowCount(0);
            for (Student s : students) {
                StringBuilder courseInfo = new StringBuilder();
                for (Map.Entry<String, String> entry : s.getCourseGrades().entrySet()) {
                    courseInfo.append(entry.getKey()).append(" (Grade: ").append(entry.getValue()).append(") ");
                }
                tableModel.addRow(new Object[]{s.getName(), s.getId(), courseInfo.toString()});
            }
        });

        tabbedPane.add("View Student Details", panel);
    }

    private static class Student {
        private final String name;
        private final String id;
        private final Map<String, String> courseGrades = new HashMap<>();

        public Student(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public void enrollCourse(String course) {
            courseGrades.putIfAbsent(course, "Not Assigned");
        }

        public void assignGrade(String course, String grade) {
            if (courseGrades.containsKey(course)) {
                courseGrades.put(course, grade);
            }
        }

        public Set<String> getCourses() {
            return courseGrades.keySet();
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public Map<String, String> getCourseGrades() {
            return courseGrades;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementSystem::new);
    }
}
