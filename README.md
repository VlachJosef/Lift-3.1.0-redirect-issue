# Lift-3.1.0-redirect-issue

To run

> jetty:start

Navigate to `http://localhost:8080/myservice/`.

Notice application is running with `myservice` context path and there is active comet session showing current date at the bottom of the page.

### Test case

Do any action which results in redirect ie. sign-in, login or logout.

### Actual result:

Browser location after redirect ends up being `http://localhost:8080`.

### Expected result:

Browser location after redirect should be `http://localhost:8080/myservice/` (context path should be taken into account).

### Cause of the bug

`cometOnSessionLost` in `lift.js` is redirecting to `"/"` discarding any context path.

```javascript
cometOnSessionLost: function() {
  window.location.href = "/";
},
```

https://github.com/lift/framework/blob/fe37149f52166988b9d73d3ed25a0b8f020f383e/web/webkit/src/main/resources/toserve/lift.js#L80

### Do it yourself fix
Add this into `Boot.scala` (or [uncomment](https://github.com/VlachJosef/Lift-3.1.0-redirect-issue/blob/master/src/main/scala/bootstrap/liftweb/Boot.scala#L90) this line):
```scala
LiftRules.noCometSessionCmd.default.set(() ⇒ JsRaw("window.location.href = '/myservice';"))
```

To make redirect with active comet actor work.

### Desired behaviour

Make Lift take care of context path on user behalf to avoid this surprising behaviour.
