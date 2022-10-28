package main;

import main.model.Course;
import main.model.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    //Method returns all courses
    @GetMapping("/courses/")
    public ResponseEntity<?> getAllCourses() {
        Iterable<Course> courseIterable = courseRepository.findAll();
        ArrayList<Course> courseArrayList = new ArrayList<>();
        for (Course course : courseIterable) {
            courseArrayList.add(course);
        }

        return new ResponseEntity<>(courseArrayList, HttpStatus.OK);
    }

    //Method returns course by id
    @GetMapping("/courses/{id}")
    public ResponseEntity<?> getCourses(@PathVariable int id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (!optionalCourse.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).eTag("id not found").body(null);
        }
        return new ResponseEntity<>(optionalCourse.get(), HttpStatus.OK);
    }

    //Method adds courses
    @PostMapping("/courses/")
    public ResponseEntity<?> addCourse(String name) {
        Course course1 = courseRepository.save(new Course(name));
        return new ResponseEntity<>(course1.getId(), HttpStatus.CREATED);
    }

    //Method update course by id
    @PutMapping("/courses/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable int id, String name) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            courseRepository.save(new Course(id, name));
            return new ResponseEntity<>("exchange course made: " + name, HttpStatus.CREATED);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).eTag("id not found").body(null);
    }

    //Method update all courses
    @PutMapping("/courses/")
    public ResponseEntity<?> updateAllCourses(String name) {
        Iterable<Course> courseIterable = courseRepository.findAll();
        ArrayList<Course> arrayList = new ArrayList<>();
        for (Course course : courseIterable) {
            course.setNameCourses(String.valueOf(name));
            arrayList.add(course);
        }
        courseRepository.saveAll(arrayList);
        return new ResponseEntity<>("exchange course made: " + name, HttpStatus.CREATED);
    }

    //Method delete by id
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable int id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            courseRepository.deleteById(id);
            return new ResponseEntity<>("course has been deleted, №" + id, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    //Method for delete all courses
    @DeleteMapping("/courses/")
    public ResponseEntity<?> deleteAllCourses() {
        courseRepository.deleteAll();
        return new ResponseEntity<>("all courses has been deleted", HttpStatus.OK);
    }
}
