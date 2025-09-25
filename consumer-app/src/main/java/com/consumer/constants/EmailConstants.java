package com.consumer.constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EmailConstants {
    public static final Set<String> EMAIL_EVENTS = new HashSet<String>(
            Arrays.asList("NCLM_JS_SERVER_SEEKER_DB_UNLOCK",
                    "NCLM_JS_SERVER_SEEKER_CALLED",
                    "NCLM_JS_SERVER_SEEKER_CALLED_RECO",
                    "NCLM_JS_SERVER_SEEKER_MESSAGED",
                    "NCLM_JS_SERVER_SEEKER_SHORTLISTED",
                    "NCLM_JS_SERVER_SEEKER_SHORTLISTED_RECO"));
}
