@Controller
public class TestController {

    public static class Model {
        public int id;
    }

    @RequestMapping(value = "/testPost", method = RequestMethod.POST, headers = "content-type=application/json")
    public @ResponseBody String testPost(@RequestBody Model model) {
        System.out.println("SuccessPost");
        System.out.println(model.id);
        return "SuccessPost";
    }
}