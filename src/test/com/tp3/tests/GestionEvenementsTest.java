@Test
public void testAjouterParticipant() {
    Conference conf = new Conference("001", "Conférence IA", ...);
    Participant p = new Participant("p1", "Jean", "jean@mail.com");

    conf.ajouterParticipant(p);

    assertTrue(conf.getParticipants().contains(p));
}
