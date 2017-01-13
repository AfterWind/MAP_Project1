package afterwind.lab1.repository.sql;

import afterwind.lab1.database.SQLiteDatabase;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.validator.IValidator;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteOptionRepository extends SQLiteRepository<Option>{

    private IRepository<Section, Integer> sectionRepo;
    private IRepository<Candidate, Integer> candidateRepo;

    public SQLiteOptionRepository(SQLiteDatabase database, IValidator<Option> validator, IRepository<Candidate, Integer> candidateRepo, IRepository<Section, Integer> sectionRepo, int entitiesPerPage) {
        super(database, validator, entitiesPerPage);
        initTable();
        this.sectionRepo = sectionRepo;
        this.candidateRepo = candidateRepo;

        statementAdd = database.getStatement("INSERT INTO Options VALUES(?, ?, ?)");
        statementRemove = database.getStatement("DELETE FROM Options WHERE ID = ?");
        statementUpdate = database.getStatement("UPDATE Options SET CandidateID = ?, SectionID = ? WHERE ID = ?");
        statementSelectAll = database.getStatement("SELECT * FROM Options");

        load();
    }

    private void initTable() {
        try {
            database.getStatement(
                    "CREATE TABLE IF NOT EXISTS Options(" +
                            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "CandidateID INTEGER," +
                            "SectionID INTEGER, " +
                            "FOREIGN KEY(CandidateID) REFERENCES Candidates(ID)," +
                            "FOREIGN KEY(SectionID) REFERENCES Sections(ID))").execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void load() {
        try {
            ResultSet result = statementSelectAll.executeQuery();
            while(result.next()) {
                try {
                    Candidate c = candidateRepo.get(result.getInt(2));
                    if (c == null) throw new ValidationException("Candidate with id "+ result.getInt(2) +" does not exist!");
                    Section s = sectionRepo.get(result.getInt(3));
                    if (s == null) throw new ValidationException("Section with id "+ result.getInt(3) +" does not exist!");

                    Option o = new Option(result.getInt(1), s, c);
                    super.add(o);
                } catch (ValidationException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Option o) throws ValidationException {
        try {
            statementAdd.setInt(1, o.getId());
            statementAdd.setInt(2, o.getCandidate().getId());
            statementAdd.setInt(3, o.getSection().getId());
            statementAdd.execute();
            super.add(o);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Integer key, Option data) {
        try {
            statementUpdate.setInt(1, data.getCandidate().getId());
            statementUpdate.setInt(2, data.getSection().getId());
            statementUpdate.setInt(3, key);
            statementUpdate.execute();
            super.update(key, data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
