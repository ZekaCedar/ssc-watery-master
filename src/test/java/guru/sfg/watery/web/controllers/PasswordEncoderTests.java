package guru.sfg.watery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncoderTests {

    static final String PASSWORD = "password";

    @Test
    void testBcrypt15(){
        PasswordEncoder bcrypt = new BCryptPasswordEncoder(15);

        System.out.println(bcrypt.encode(PASSWORD));
        System.out.println(bcrypt.encode(PASSWORD));
        System.out.println(bcrypt.encode("tiger"));
    }

    /**
     * Implementation of PasswordEncoder that uses the BCrypt strong hashing function. Clients
     * can optionally supply a "version" ($2a, $2b, $2y) and a "strength" (a.k.a. log rounds in BCrypt)
     * and a SecureRandom instance. The larger the strength parameter the more work will have to be done
     * (exponentially) to hash the passwords. The default value is 10.
     *
     * @author Dave Syer
     */
    @Test
    void testBcrypt(){
        PasswordEncoder bcrypt = new BCryptPasswordEncoder();

        System.out.println(bcrypt.encode(PASSWORD));
        System.out.println(bcrypt.encode(PASSWORD));
        System.out.println(bcrypt.encode("guru"));
    }

    /**
     * This {@link PasswordEncoder} is provided for legacy purposes only and is not considered
     * secure.
     *
     * A standard {@code PasswordEncoder} implementation that uses SHA-256 hashing with 1024
     * iterations and a random 8-byte random salt value. It uses an additional system-wide
     * secret value to provide additional protection.
     * <p>
     * The digest algorithm is invoked on the concatenated bytes of the salt, secret and
     * password.
     * <p>
     * If you are developing a new system,
     * {@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder} is a better
     * choice both in terms of security and interoperability with other languages.
     *
     * @author Keith Donald
     * @author Luke Taylor
     * @deprecated Digest based password encoding is not considered secure. Instead use an
     * adaptive one way function like BCryptPasswordEncoder, Pbkdf2PasswordEncoder, or
     * SCryptPasswordEncoder. Even better use {@link DelegatingPasswordEncoder} which supports
     * password upgrades. There are no plans to remove this support. It is deprecated to indicate
     * that this is a legacy implementation and using it is considered insecure.
     */
    @Test
    void testSha256(){
        PasswordEncoder sha256 = new StandardPasswordEncoder();

        System.out.println(sha256.encode(PASSWORD));
        System.out.println(sha256.encode(PASSWORD));

    }

    /**
     * This {@link PasswordEncoder} is provided for legacy purposes only and is not considered
     * secure.
     *
     * A version of {@link PasswordEncoder} which supports Ldap SHA and SSHA (salted-SHA)
     * encodings. The values are base-64 encoded and have the label "{SHA}" (or "{SSHA}")
     * prepended to the encoded hash. These can be made lower-case in the encoded password, if
     * required, by setting the <tt>forceLowerCasePrefix</tt> property to true.
     *
     * Also supports plain text passwords, so can safely be used in cases when both encoded
     * and non-encoded passwords are in use or when a null implementation is required.
     *
     * @author Luke Taylor
     * @deprecated Digest based password encoding is not considered secure. Instead use an
     * adaptive one way function like BCryptPasswordEncoder, Pbkdf2PasswordEncoder, or
     * SCryptPasswordEncoder. Even better use {@link DelegatingPasswordEncoder} which supports
     * password upgrades. There are no plans to remove this support. It is deprecated to indicate
     * that this is a legacy implementation and using it is considered insecure.
     */
    @Test
    void testLdap(){
        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode("tiger"));

        String encodedPassword = ldap.encode(PASSWORD);
        System.out.println(encodedPassword);

        assertTrue(ldap.matches(PASSWORD, encodedPassword));
    }

    /**
     *This PasswordEncoder is provided for legacy and testing purposes only and is not considered secure. A password encoder that does nothing. Useful for testing where working with plain text passwords may be preferred.
     */
    @Test
    void TestNoOp(){
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();

        System.out.println(noOp.encode(PASSWORD));
    }

    /**
     * Not Random
     * The salt is affecting the output of the Hash
     * It is the same value in generating password
     * always evaluate to the exact same value
     * Not Recommended
     * Demonstrate how Hashing works
     */
    @Test
    void hashingExample(){
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        String salted = PASSWORD + "THISiSmYsaltVALUE";
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
    }

}
