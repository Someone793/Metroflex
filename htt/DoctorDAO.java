/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */
/*
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {
    // Method to add a doctor
    public void addDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctors (name, specialization, phone, email, schedule) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getPhone());
            stmt.setString(4, doctor.getEmail());
            stmt.setString(5, doctor.getSchedule());

            stmt.executeUpdate();
            System.out.println("✅ Doctor added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get all doctors
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors";
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                doctors.add(new Doctor(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("specialization"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("schedule")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }
}
*/
