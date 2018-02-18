class SmartThingsBuildHelper {
    static String to_token(file_name) {
        "__${file_name.split('\\.')[0].toUpperCase()}__".toString()
    }

    static void compile(main_file_name, String source_path, String output_path) {
        def file_tokens = [:]
        def source_dir = new File(source_path)
        source_dir.eachFile { file ->
            file_tokens[to_token(file.name)] = file.text
        }

        def applied_tokens = []
        def output = new File(output_path)
        def outText = file_tokens[to_token(main_file_name)].toString()

        // Ghetto import system.  Use __FILENAME__ to import
        boolean new_import = false
        while (true) {
            file_tokens.each { token, tokenText ->
                if (outText.indexOf(token) > -1) {
                    if (!applied_tokens.contains(token)) {
                        applied_tokens << token
                        try {
                            outText = outText.replace(token.toString(), tokenText.toString())
                        } catch (e) {
                            print e
                        }
                        new_import = true
                    }
                }
            }
            if (!new_import) {
                break
            }
            new_import = false
        }

        output.write(outText.toString())
    }
}