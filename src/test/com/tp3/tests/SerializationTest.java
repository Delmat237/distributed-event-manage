@Test
public void testCapaciteMaxException() {
    Conference conf = new Conference("001", "IA", ..., 1);
    Participant p1 = new Participant("p1", "Alice", "a@mail.com");
    Participant p2 = new Participant("p2", "Bob", "b@mail.com");

    conf.ajouterParticipant(p1);

    assertThrows(CapaciteMaxAtteinteException.class, () -> {
        conf.ajouterParticipant(p2);
    });
}
