## How to run
java -jar <jar file name>

### Properties
config.properties should have all properties filled in.

### Acceptable runtime arguments
"loopon" - indicate to run application continiously with interval specified in config.properties - e.g. java -jar <jar file name> loopon

## How to build
mvn package

## Backend service integration (example)

### Controller
`@RequestMapping(value = "/testPost", method = RequestMethod.POST, headers = "content-type=application/json")
      public
      @ResponseBody
      String testPost(@RequestBody List<Result> results) {
          System.out.println("SuccessPost");
          System.out.println(results.toString());
          return "SuccessPost";
      }`
### Model
`public static class Result {
          public Result() {
          }
  
          public int id;
  
          public int getId() {
              return id;
          }
  
          public void setId(int id) {
              this.id = id;
          }
  
          public String getOldValue() {
              return oldValue;
          }
  
          public void setOldValue(String oldValue) {
              this.oldValue = oldValue;
          }
  
          public String getNewValue() {
              return newValue;
          }
  
          public void setNewValue(String newValue) {
              this.newValue = newValue;
          }
  
          public String oldValue;
          public String newValue;
  
          @Override
          public String toString() {
              return "" + id + "," + oldValue + "," + newValue;
          }
      }`