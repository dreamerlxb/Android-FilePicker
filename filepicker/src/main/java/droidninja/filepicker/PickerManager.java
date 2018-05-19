package droidninja.filepicker;

import java.util.ArrayList;

import droidninja.filepicker.models.BaseFile;
import droidninja.filepicker.models.FileType;
import droidninja.filepicker.models.sort.SortingTypes;
import droidninja.filepicker.utils.Orientation;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by droidNinja on 29/07/16.
 */
public class PickerManager {
    private static PickerManager ourInstance = new PickerManager();
    private int maxCount = FilePickerConst.DEFAULT_MAX_COUNT;
    private SortingTypes sortingType = SortingTypes.none;

    public static PickerManager getInstance() {
        return ourInstance;
    }

    private ArrayList<String> docFiles;

    private LinkedHashSet<FileType> fileTypes;

    private int theme = R.style.LibAppTheme;

    private String title = null;

    private boolean showSelectAll = false;

    private boolean docSupport = true;

    private Orientation orientation = Orientation.UNSPECIFIED;

    private boolean showFolderView = true;

    private String providerAuthorities;

    private PickerManager() {
        docFiles = new ArrayList<>();
        fileTypes = new LinkedHashSet<>();
    }

    public void setMaxCount(int count) {
        reset();
        this.maxCount = count;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getCurrentCount() {
        return docFiles.size();
    }

    public void add(String path, int type) {
        if (path != null && shouldAdd()) {
            if (!docFiles.contains(path) && type == FilePickerConst.FILE_TYPE_DOCUMENT) {
                docFiles.add(path);
            }
        }
    }

    public void add(ArrayList<String> paths, int type) {
        for (int index = 0; index < paths.size(); index++) {
            add(paths.get(index), type);
        }
    }

    public void remove(String path, int type) {
        if (type == FilePickerConst.FILE_TYPE_DOCUMENT) {
            docFiles.remove(path);
        }
    }

    public boolean shouldAdd() {
        if (maxCount == -1) return true;
        return getCurrentCount() < maxCount;
    }

    public ArrayList<String> getSelectedFiles() {
        return docFiles;
    }

    public ArrayList<String> getSelectedFilePaths(ArrayList<BaseFile> files) {
        ArrayList<String> paths = new ArrayList<>();
        for (int index = 0; index < files.size(); index++) {
            paths.add(files.get(index).getPath());
        }
        return paths;
    }

    public void reset() {
        docFiles.clear();
        fileTypes.clear();
        maxCount = -1;
    }

    public void clearSelections() {
        docFiles.clear();
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isShowFolderView() {
        return showFolderView;
    }

    public void setShowFolderView(boolean showFolderView) {
        this.showFolderView = showFolderView;
    }

    public void addFileType(FileType fileType) {
        fileTypes.add(fileType);
    }

    public void addDocTypes() {
        String[] pdfs = {"pdf"};
        fileTypes.add(new FileType(FilePickerConst.PDF, pdfs, R.drawable.icon_file_pdf));

        String[] docs = {"doc", "docx", "dot", "dotx"};
        fileTypes.add(new FileType(FilePickerConst.DOC, docs, R.drawable.icon_file_doc));

        String[] ppts = {"ppt", "pptx"};
        fileTypes.add(new FileType(FilePickerConst.PPT, ppts, R.drawable.icon_file_ppt));

        String[] xlss = {"xls", "xlsx"};
        fileTypes.add(new FileType(FilePickerConst.XLS, xlss, R.drawable.icon_file_xls));

        String[] txts = {"txt"};
        fileTypes.add(new FileType(FilePickerConst.TXT, txts, R.drawable.icon_file_unknown));
    }

    public ArrayList<FileType> getFileTypes() {
        return new ArrayList<>(fileTypes);
    }

    public boolean isDocSupport() {
        return docSupport;
    }

    public void setDocSupport(boolean docSupport) {
        this.docSupport = docSupport;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public String getProviderAuthorities() {
        return providerAuthorities;
    }

    public void setProviderAuthorities(String providerAuthorities) {
        this.providerAuthorities = providerAuthorities;
    }

    public boolean hasSelectAll() {
        return maxCount == -1 && showSelectAll;
    }

    public void enableSelectAll(boolean showSelectAll) {
        this.showSelectAll = showSelectAll;
    }

    public SortingTypes getSortingType() {
        return sortingType;
    }

    public void setSortingType(SortingTypes sortingType) {
        this.sortingType = sortingType;
    }
}
