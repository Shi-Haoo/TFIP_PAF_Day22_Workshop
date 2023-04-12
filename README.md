
In RSVPRepository:

KeyHolder keyHolder = new GeneratedKeyHolder();
        if (newRsvp == null) {
            // insert record
            JdbcTemplate.update(conn -> {
                PreparedStatement statement = conn.prepareStatement(INSERT_NEW_RSVP, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, rsvp.getName());
                statement.setString(2, rsvp.getEmail());
                statement.setString(3, rsvp.getPhone());
                statement.setTimestamp(4, new Timestamp(rsvp.getConfirmationDate().toDateTime().getMillis()));
                statement.setString(5, rsvp.getComments());
                return statement;

            }, keyHolder);

        BigInteger primaryKey = (BigInteger) keyHolder.getKey();
        rsvp.setId(primaryKey.intValue());
            }

In the code above, JdbcTemplate.update() has 2 parameters with the first one being a lambda expression and the 2nd parameter is keyholder.

JdbcTemplate.update expects a PreparedStatementCreator as input/parameter. The lambda function is used as a shorthand way of implementing the PreparedStatementCreator interface.
By returning the PreparedStatement from the lambda function, it is executed by the update method, which sends the SQL statement to the database to insert a new record. 

Statement.RETURN_GENERATED_KEYS is passed into prepareStatement method to indicate that the statement should return any auto-generated keys.

The generated keys are then stored in the KeyHolder object. This is why we pass in keyholder as the 2nd parameter.

qn: What does setString(number,value) mean?
Ans: In this case, setString(1, rsvp.getName()) sets the first placeholder (?) in the SQL statement to the value of rsvp.getName(). The number 1 represents the index of the placeholder, which corresponds to the first parameter in the SQL statement.

Qn: Why do we use BigInteger?
Ans: BigInteger is that it is capable of holding much larger values than an int or long can hold, which may be necessary in some cases where the primary key values are very large. Additionally, it allows for consistent handling of the returned value regardless of the size of the value, ensuring that it can be safely used in subsequent database operations if needed.

We convert BigInteger to int using intValue(). But if the BigInteger value is too large to fit in an int, then the conversion will result in a loss of precision. We may want to use the longValue() method instead to convert the BigInteger to a long, which has a larger range than int.
But since the variable id we set it to be of type Integer, we convert to integer

--------------------------------------------------------------------------------------------------------

Under RSVP.java:

cannot use the code below to convert from string to DateTime object if your datetime input is in the format of "dd-MM-yyyy"
ISODateTimeFormat.dateTime() method expects a string in the ISO date-time format, which is "yyyy-MM-dd'T'HH:mm:ss.SSSZ".

DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
DateTime date = formatter.parseDateTime(o.getString("confirmationDate"));
rsvp.setConfirmationDate(date);

To parse a date string in the format "dd-mm-yyyy" into a DateTime object using Joda-Time, We need to use this:

DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
DateTime date = formatter.parseDateTime(o.getString("confirmationDate"));
rsvp.setConfirmationDate(date);

The resulting DateTime object will represent the date and time parsed from the input string, with the time zone set to the system default time zone of the Java runtime environment.

For example, if the input string is "2023-02-16T14:30:00Z", the resulting DateTime object will represent February 16, 2023, at 14:30:00 UTC (Coordinated Universal Time). If the system default time zone is, say, Pacific Standard Time, then the DateTime object will represent February 16, 2023, at 06:30:00 PST (Pacific Standard Time).
-----------------------------------------------------------------------------------------

In RSVPRepository:

1.  template.queryForObject(SELECT_RSVP_COUNT, Long.class);

- When only a single row of result is expected, we can use queryForObject
- There is no need for rowmapper in this case because queryForObject method can automatically handle simple cases like this where the query returns a single value that can be mapped directly to a Java object.

If the query result is more complex, such as multiple rows or columns that need to be mapped to a custom Java object, then a row mapper would need to be provided.

Example:
public class Student {
    private int id;
    private String name;
    private int age;

    // getters and setters
}

String sql = "SELECT id, name, age FROM students WHERE id = ?";
int id = 1;
Student student = jdbcTemplate.queryForObject(sql, new Object[]{id}, new RowMapper<Student>() {
    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setName(rs.getString("name"));
        student.setAge(rs.getInt("age"));
        return student;
    }
});

2.
    List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_RSVP_COUNT);
    return (Long) rows.get(0).get("total_count");

    - queryForList returns a list of maps, where each map represents a row of the result set. The keys of the map correspond to the column names and the values correspond to the column values for that row. 
    So in this case, since the query returns a single row with a single column total_count, the list returned by queryForList will contain a single map with a key "total_count" and the count value as its value.

    -  if there are multiple rows returned by the query, then each row will be represented as a separate map in the rows list. Each map will contain the values for the columns in that particular row. So if there are multiple rows under the total_count column, then each row will be represented as a separate map, with the total_count value for that row stored under the total_count key in that map.

    - rows.get(0).get("total_count") => Getting the Map object stored in the 1st index of the list aka first row of data. And then retrieving the value mapped to the key "total_count"
-------------------------------------------------------------------------------------------------------------------

Under RSVPRestController:

@PostMapping(path="/rsvp/form", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> upsertViaForm(@ModelAttribute RSVP rsvp, String date){

        DateTime confirmationDate = RSVP.convertToDateTimeFromForm(date);
        rsvp.setConfirmationDate(confirmationDate);
        ...}

We cannot directly bind the date in html to confirmationDate field in RSVP.java using thymeleaf because date is of type String while confirmationDate is of type DateTime.

So we need to retrieve the value of date stored in payload when we submit the form. Subsequently, we convert it to DateTime and then set it into the RSVP object

In the form, we assigned the name for confirmation date as "date". So to retrieve the value of confirmation date, we just use String date in the parameter. It will be mapped to the value of date in the form