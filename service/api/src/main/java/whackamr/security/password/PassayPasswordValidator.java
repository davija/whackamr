package whackamr.security.password;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link PasswordValidator} that uses Passay to validate passwords meet rule requirements.
 * 
 * @author James Davis
 */
@Component
public class PassayPasswordValidator implements PasswordValidator
{
    private org.passay.PasswordValidator validator;

    /**
     * Creates a new password valdiator.
     */
    public PassayPasswordValidator()
    {
        validator = new org.passay.PasswordValidator(
                                                     new LengthRule(8, 200),
                                                     new CharacterRule(EnglishCharacterData.UpperCase, 1),
                                                     new CharacterRule(EnglishCharacterData.LowerCase, 1),
                                                     new CharacterRule(EnglishCharacterData.Digit, 1),
                                                     new CharacterRule(EnglishCharacterData.Special, 1));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(String password)
    {
        var result = validator.validate(new PasswordData(password));

        if (!result.isValid())
        {
            throw new PasswordConstraintViolationException(validator.getMessages(result));
        }
    }
}