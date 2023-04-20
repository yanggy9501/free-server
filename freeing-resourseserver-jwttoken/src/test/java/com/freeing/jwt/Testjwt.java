package com.freeing.jwt;

/**
 * @author yanggy
 */
public class Testjwt {
    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsieHVlY2hlbmctcGx1cyJdLCJvdGhlciI6Inh4eCIsInVzZXJfbmFtZSI6InpoYW5nc2FuIiwibG92ZXIiOiJqd3QiLCJzY29wZSI6WyJhbGwiXSwic2V4IjoibSIsImV4cCI6MTY4MTk4MjcyNiwiYXV0aG9yaXRpZXMiOlsicDEiXSwianRpIjoiYTdhZDJlMDMtY2Q4ZC00MDFlLTg0Y2UtMTg1ZWJmZjNmMDdmIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.gGTz33GSysxTRonVd1NH0iByq-6YJoZyuVdkGczxkJI";
//        Jws<Claims> claimsJws = Jwts.parser().setSigningKey("123").parseClaimsJws(token);
//        System.out.println(claimsJws);

        String kato = JwtUtils.createToken("520", "kato", "123abcd", 500000);
        String t2 = kato;
        System.out.println(JwtUtils.checkToken(token, "123abcd"));
    }
}
