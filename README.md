# ksuid - Java Version

A Java implementation of the [Segment's KSUID library](https://github.com/segmentio/ksuid). For a full treatment of why this came about, you can read the [blog post](https://segment.com/blog/a-brief-history-of-the-uuid/) on their website.

# Maven Central
At this time, the library is not published to Maven Central. This is work in progress.

#Quick Start
``` java
final Ksuid ksuid = new Ksuid();
final String uid = ksuid.generate();
// e.g. output: Be785NYYxP29BJiAJPupfsXuGpR

final String decoded = ksuid.parse(uid);
// e.g. output: Time: 2017-07-08T21:13:08Z[UTC]
//              Timestamp: 1499548388
//              Payload: [-42, 24, -60, -3, -66, 38, 32, 9, 62, -22, 95, -79, 123, -122, -91, 0] 
```

# History
- 1.0.0: Initial public release.
