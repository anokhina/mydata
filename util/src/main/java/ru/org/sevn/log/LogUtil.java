/*
 * Copyright 2024 Veronica Anokhina.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.org.sevn.log;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Veronica Anokhina
 */
public class LogUtil {

    public static void info (Logger log, String msg) {
        info (log, () -> msg);
    }

    public static void info (Logger log, Supplier<String> msg) {
        log.log (Level.INFO, msg);
    }

    public static void warning (Logger log, String msg, Throwable tr) {
        throwable (log, Level.WARNING, msg, tr);
    }

    public static void warning (Logger log, Supplier<String> msg, Supplier<Throwable> tr) {
        throwable (log, Level.WARNING, msg, tr);
    }

    public static void error (Logger log, Throwable tr) {
        error (log, null, tr);
    }

    public static void error (Logger log, String msg, Throwable tr) {
        throwable (log, Level.SEVERE, msg, tr);
    }

    public static void error (Logger log, Supplier<String> msg, Supplier<Throwable> tr) {
        throwable (log, Level.SEVERE, msg, tr);
    }

    public static void throwable (Logger log, Level level, Supplier<String> msg, Supplier<Throwable> tr) {
        if (tr == null) {
            log.log (level, msg);
        }
        else {
            log.log (level, tr.get (), msg);
        }
    }

    public static void throwable (Logger log, Level level, String msg, Throwable tr) {
        log.log (level, msg, tr);
    }
}
