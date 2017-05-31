package org.zalando.nakadi.validation;

import java.time.OffsetDateTime;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.everit.json.schema.FormatValidator;

public class RFC3339DateTimeValidator implements FormatValidator {

    private final String errorMessage = "must be a valid date-time";

    // Valid offsets are either Z or hh:mm. The format hh:mm:ss is not valid
    private final String dateTimeOffsetPattern = "^.*T\\d{2}:\\d{2}:\\d{2}.*(Z|((\\+|-)\\d{2}:\\d{2}))$";
    private final Pattern pattern = Pattern.compile(dateTimeOffsetPattern);
    private final Optional<String> error = Optional.of(errorMessage);

    @Override
    public Optional<String> validate(final String dateTime) {
        try {
            OffsetDateTime.parse(dateTime, ISO_OFFSET_DATE_TIME);

            // Unfortunately, ISO_OFFSET_DATE_TIME accepts offsets with seconds, which is not RFC3339 compliant. So
            // we need to do some further checks using a regex in order to be sure that it adhere to the given format.
            final Matcher matcher = pattern.matcher(dateTime);
            if (matcher.matches()) {
                return Optional.empty();
            } else {
                return error;
            }

        } catch (final DateTimeParseException e) {
            return error;
        }
    }
}
