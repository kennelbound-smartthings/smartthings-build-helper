# smartthings-build-helper

My projects with SmartThings are continuing to get larger and larger.  Particularly when doing unofficial integrations, there might be dozens of components I need to manage for a single device handler.  Putting all of this in a single file was getting difficult to manage.

So I build a very simple groovy helper that uses token replacement to manage file-level dependencies.  SmartThings doesn't allow classes, but with some tricks from the old days of coding (prefixes, primarily) we can get pretty far.

Note that this doesn't support fetching remote dependencies (i.e. files from other repos) but someone could pretty easily add that in.

Lastly, I've included an example to show how you can use this inside your own project.  I wrote it such that you don't need to include the SmartThingsBuildHelper.groovy file in your source (though you might need the build.groovy or something like it committed.)

## Tokens

The SmartThingsBuildHelper (STBH) uses token replacement to "inject" file contents within a single template.  These tokens are generated from the filename (without file extension) uppercased and with two underlines as prefix and suffix.  For instance, if I had a project that looked like:

```
 /
  . build.groovy
  . src/
         . device_handler.groovy
         . client.groovy 
```
 
My device_handler.groovy would probably look something like this:

```groovy
metadata {
    definition(name: "KB: eero Mesh Network", namespace: "kennelbound-smartthings/eero-unofficial", author: "Kennelbound") {
        capability "Actuator"
        capability "Sensor"

        attribute "name", "string"
        attribute "version", "string"
        attribute "uploadSpeedMbps", "number"
        attribute "downloadSpeedMbps", "number"
    }
}

__CLIENT__
```

If client.groovy, looked like:

```groovy
def myFunction() {
    return "dosomething"
}
```

Then the output would look like:

```groovy
metadata {
    definition(name: "KB: eero Mesh Network", namespace: "kennelbound-smartthings/eero-unofficial", author: "Kennelbound") {
        capability "Actuator"
        capability "Sensor"

        attribute "name", "string"
        attribute "version", "string"
        attribute "uploadSpeedMbps", "number"
        attribute "downloadSpeedMbps", "number"
    }
}

def myFunction() {
    return "dosomething"
}
```

It can handle multi-level dependencies (i.e. a->b->c) and will only import a token once (since with groovy the order of the definitions doesn't matter, we only need to import items once)

# Example
There's a runnable example in the example dir.  With groovy installed (and in your path) run from the example dir:

```bash
groovy build
```

It will output the eero-device_handler.groovy and eero-smartapp.groovy files.