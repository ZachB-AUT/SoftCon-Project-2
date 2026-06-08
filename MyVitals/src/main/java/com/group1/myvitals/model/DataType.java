public abstract class DataType {

    private String DataTypeName;
    private int DataTypeId;
    private static int number_of_datatypes = 0;

    public DataType(String DataTypeName) {
        number_of_datatypes++;
        this.DataTypeId = number_of_datatypes;
        this.DataTypeName = DataTypeName;
    }

    /**
     * Returns a string appropriate for inserting this datatype into the database.
     * @return
     */
    public String db_string() {
        return String.format("%s", DataTypeName);
    }
}
